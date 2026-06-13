package com.resq.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Repl {

    @Value("${repl.token:}")
    private String tok;

    @Value("${repl.model:openai/gpt-5.4}")
    private String mdl;

    @Value("${repl.enabled:true}")
    private boolean on;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final ObjectMapper om = new ObjectMapper();

    public boolean ready() {
        return on && tok != null && !tok.isBlank();
    }

    public String host(byte[] data) throws Exception {
        String bd = "resq" + System.currentTimeMillis();
        java.io.ByteArrayOutputStream b = new java.io.ByteArrayOutputStream();
        String pre = "--" + bd + "\r\nContent-Disposition: form-data; name=\"reqtype\"\r\n\r\nfileupload\r\n"
                + "--" + bd + "\r\nContent-Disposition: form-data; name=\"fileToUpload\"; filename=\"d.jpg\"\r\n"
                + "Content-Type: image/jpeg\r\n\r\n";
        String post = "\r\n--" + bd + "--\r\n";
        b.write(pre.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        b.write(data);
        b.write(post.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        HttpRequest req = HttpRequest.newBuilder(URI.create("https://catbox.moe/user/api.php"))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "multipart/form-data; boundary=" + bd)
                .POST(HttpRequest.BodyPublishers.ofByteArray(b.toByteArray()))
                .build();
        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
        String url = res.body() == null ? "" : res.body().trim();
        if (!url.startsWith("http")) {
            throw new IllegalStateException("upload failed");
        }
        return url;
    }

    public String run(String sys, String ask) throws Exception {
        return run(sys, ask, java.util.List.of());
    }

    public String run(String sys, String ask, java.util.List<String> imgs) throws Exception {
        if (!ready()) {
            throw new IllegalStateException("AI off");
        }
        Map<String, Object> in = new LinkedHashMap<>();
        in.put("prompt", ask);
        in.put("system_prompt", sys);
        in.put("reasoning_effort", "low");
        in.put("verbosity", "low");
        in.put("max_completion_tokens", 1400);
        if (imgs != null && !imgs.isEmpty()) {
            in.put("image_input", imgs);
        }
        String body = om.writeValueAsString(Map.of("input", in));
        String url = "https://api.replicate.com/v1/models/" + mdl + "/predictions";
        Exception last = null;
        for (int i = 0; i < 3; i++) {
            try {
                HttpRequest req = HttpRequest.newBuilder(URI.create(url))
                        .timeout(Duration.ofSeconds(120))
                        .header("Authorization", "Bearer " + tok)
                        .header("Content-Type", "application/json")
                        .header("Prefer", "wait")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
                String b = res.body();
                if (b == null || b.isBlank()) {
                    throw new IllegalStateException("empty body");
                }
                JsonNode node = poll(om.readTree(b));
                return text(node);
            } catch (Exception e) {
                last = e;
                Thread.sleep(1000);
            }
        }
        throw last != null ? last : new IllegalStateException("AI failed");
    }

    private JsonNode poll(JsonNode node) throws Exception {
        int n = 0;
        while (n < 60) {
            String st = node.path("status").asText("");
            if ("succeeded".equals(st)) {
                return node;
            }
            if ("failed".equals(st) || "canceled".equals(st)) {
                throw new IllegalStateException("AI " + st + ": " + node.path("error").asText(""));
            }
            String get = node.path("urls").path("get").asText("");
            if (get.isBlank()) {
                throw new IllegalStateException("AI no url");
            }
            Thread.sleep(2000);
            HttpRequest req = HttpRequest.newBuilder(URI.create(get))
                    .timeout(Duration.ofSeconds(30))
                    .header("Authorization", "Bearer " + tok)
                    .GET()
                    .build();
            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            node = om.readTree(res.body());
            n++;
        }
        throw new IllegalStateException("AI timeout");
    }

    private String text(JsonNode node) {
        JsonNode out = node.path("output");
        if (out.isArray()) {
            StringBuilder b = new StringBuilder();
            for (JsonNode p : out) {
                b.append(p.asText(""));
            }
            return b.toString();
        }
        return out.asText("");
    }
}
