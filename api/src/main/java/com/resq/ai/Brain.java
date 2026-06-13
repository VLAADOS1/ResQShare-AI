package com.resq.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resq.dto.AnaIn;
import com.resq.dto.AnaOut;
import com.resq.type.Safe;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Brain {

    private static final String SYS =
            "You are the listing and safety engine for ResQShare, an app that safely redistributes "
            + "surplus food and essentials. Given one donation (details and possibly a photo), identify "
            + "the item and write a clean, friendly PUBLIC listing. Reply with ONLY a JSON object, no markdown. "
            + "Schema: {\"item\":string,\"title\":string,\"descr\":string,"
            + "\"safe\":one of [SAFE,SOON,ORGONLY,REVIEW,UNSAFE,RECYCLE],"
            + "\"risk\":integer 0-100,\"expl\":string,\"checks\":string[],\"recs\":string[]}. "
            + "descr: a short positive public description (1-2 sentences) as if advertising the item to a recipient. "
            + "If some details are unknown, simply omit them or add a brief neutral note like 'condition to be confirmed at pickup'. "
            + "NEVER apologize, NEVER say information is missing in a complaining tone, and NEVER ask the user to clarify or confirm anything. "
            + "safe meaning: SAFE=packaged and stable; SOON=perishable, donate soon; ORGONLY=prepared/perishable, route via organizations; "
            + "REVIEW=condition unclear; UNSAFE=clear hazard; RECYCLE=not reusable. risk higher for opened/prepared/hot/expired items. "
            + "checks: 4-6 short YES/NO verification questions the donor can confirm about safety/condition "
            + "(e.g. 'Packaging sealed?', 'No major tears or broken zippers?', 'Within use-by date?'). "
            + "recs: recipient types (shelters, food banks, families, students). Keep title under 60 chars, descr under 200 chars. "
            + "Do NOT invent allergens or expiry dates you were not given.";

    private static final String DSYS =
            "You are a community needs analyst for a surplus food and essentials redistribution app. "
            + "Reply with ONE short, concrete sentence naming what is most needed. No preamble, no markdown.";

    private static final String IT =
            " Write all human-readable string values (item, title, descr, expl, checks, recs) in Italian.";

    private final Repl repl;
    private final ObjectMapper om = new ObjectMapper();

    public boolean ready() {
        return repl.ready();
    }

    public AnaOut ana(AnaIn in) throws Exception {
        boolean it = "IT".equalsIgnoreCase(in.getLang());
        String sys = it ? SYS + IT : SYS;
        java.util.List<String> imgs = java.util.List.of();
        if (in.getImg() != null && !in.getImg().isBlank()) {
            try {
                imgs = java.util.List.of(repl.host(decode(in.getImg())));
            } catch (Exception e) {
                imgs = java.util.List.of(in.getImg());
            }
        }
        String raw = repl.run(sys, ask(in), imgs);
        JsonNode j = om.readTree(clip(raw));
        return AnaOut.builder()
                .item(str(j, "item", "Donation items"))
                .title(str(j, "title", "Donation"))
                .descr(str(j, "descr", ""))
                .cat(in.getCat())
                .safe(safe(j.path("safe").asText("REVIEW")))
                .risk(Math.max(0, Math.min(100, j.path("risk").asInt(25))))
                .expl(str(j, "expl", ""))
                .checks(arr(j, "checks"))
                .quests(new ArrayList<>())
                .recs(arr(j, "recs"))
                .build();
    }

    public com.resq.dto.ScanOut scan(com.resq.dto.ScanIn in) throws Exception {
        boolean it = "IT".equalsIgnoreCase(in.getLang());
        java.util.List<String> imgs = java.util.List.of();
        if (in.getImg() != null && !in.getImg().isBlank()) {
            try {
                imgs = java.util.List.of(repl.host(decode(in.getImg())));
            } catch (Exception e) {
                imgs = java.util.List.of(in.getImg());
            }
        }
        String sys = "You are the intake assistant of ResQShare, an app for donating surplus food and essentials. "
                + "Look at the photo of an item a donor wants to give away and PRE-FILL the donation form for them. "
                + "Reply with ONLY a JSON object, no markdown. Schema: {\"item\":string,\"clbl\":string,"
                + "\"cat\":one of [FOOD,CLOTH,HYG,BABY,SCHOOL,OTHER],\"qty\":string,\"cond\":string,\"note\":string}. "
                + "clbl: short human category label (e.g. 'Winter clothes', 'Fresh bread'). "
                + "qty: your best estimate of the amount visible (e.g. '3 bags', 'about 2 kg', '5 items'). "
                + "cond: short condition phrase (e.g. 'sealed, good', 'gently used'). "
                + "note: one helpful sentence describing the item for matching. Be concrete and brief; NEVER refuse or ask to clarify."
                + (it ? " Write clbl, qty, cond and note in Italian." : "");
        JsonNode j = om.readTree(clip(repl.run(sys, "Identify and pre-fill the form from the attached photo.", imgs)));
        return com.resq.dto.ScanOut.builder()
                .item(str(j, "item", "Donation items"))
                .clbl(str(j, "clbl", ""))
                .cat(cat(j.path("cat").asText("OTHER")))
                .qty(str(j, "qty", ""))
                .cond(str(j, "cond", ""))
                .note(str(j, "note", ""))
                .build();
    }

    private String cat(String s) {
        try {
            return com.resq.type.Cat.valueOf(s.trim().toUpperCase()).name();
        } catch (Exception e) {
            return "OTHER";
        }
    }

    public String req(com.resq.dom.Req r, String lang) throws Exception {
        boolean it = "IT".equalsIgnoreCase(lang);
        String sys = "You write a short, warm PUBLIC request announcement for ResQShare, an app where people "
                + "in need ask for surplus food and essentials and donors offer to help. Given the need, write "
                + "1-2 friendly sentences a donor will read, describing what is needed and how it would help. "
                + "Stay anonymous: do NOT invent names or personal details and do NOT reveal identity. "
                + "Reply with ONLY the announcement text, no markdown, no quotes."
                + (it ? " Write in Italian." : "");
        String cat = r.getCat() == null ? "Support" : r.getCat().name();
        String q = "Category: " + cat + "\n"
                + "Need: " + nv(r.getDescr()) + "\n"
                + "Quantity / household: " + nv(r.getQty()) + "\n"
                + "Urgency: " + (r.getUrg() == null ? "MED" : r.getUrg().name()) + "\n"
                + "Available: " + nv(r.getAvail()) + "\n"
                + "Notes: " + nv(r.getRestr());
        return repl.run(sys, q).trim();
    }

    public String demand(String area, String lang) throws Exception {
        String a = area == null || area.isBlank() ? "this area" : area.trim();
        String sys = "IT".equalsIgnoreCase(lang) ? DSYS + " Reply in Italian." : DSYS;
        String q = "Area: " + a + ". What surplus food and essential items are most needed here right now?";
        return repl.run(sys, q).trim();
    }

    public List<String> why(String item, String cat, List<String> cand, boolean it) throws Exception {
        StringBuilder q = new StringBuilder("Donation: ").append(item)
                .append(" (category ").append(cat).append(").\nCandidates (recipients/organizations):\n");
        for (int i = 0; i < cand.size(); i++) {
            q.append(i + 1).append(". ").append(cand.get(i)).append("\n");
        }
        String sys = "You are the matching engine of a surplus-redistribution app. For EACH candidate, in the SAME order, "
                + "write ONE short sentence (max 12 words) on why it is a good match for this donation - mention proximity, "
                + "category fit, need or urgency. Reply with ONLY a JSON array of strings of length " + cand.size()
                + ", no markdown." + (it ? " Write the sentences in Italian." : "");
        String raw = repl.run(sys, q.toString());
        JsonNode j = om.readTree(clipArr(raw));
        List<String> out = new ArrayList<>();
        if (j.isArray()) {
            for (JsonNode n : j) {
                out.add(n.asText("").trim());
            }
        }
        return out;
    }

    public List<com.resq.dto.RankOut> rank(String item, String cat, String info, List<String> cand, boolean it)
            throws Exception {
        StringBuilder q = new StringBuilder("Donation: ").append(item).append(" (category ").append(cat).append(").");
        if (info != null && !info.isBlank()) {
            q.append(" Details: ").append(info).append('.');
        }
        q.append("\nCandidates (recipients/organizations), in order:\n");
        for (int i = 0; i < cand.size(); i++) {
            q.append(i + 1).append(". ").append(cand.get(i)).append("\n");
        }
        String sys = "You are the matching engine of ResQShare, a surplus-redistribution app. Score how good EACH "
                + "candidate is for THIS donation from 0 to 100, weighing category fit, the recipient's stated needs "
                + "and diet, urgency, proximity, perishability (route perishable or prepared food to organizations, "
                + "not individuals), and household size. Reply with ONLY a JSON array (SAME order, length "
                + cand.size() + ") of objects {\"score\":integer 0-100,\"reason\":one short sentence}, no markdown."
                + (it ? " Write each reason in Italian." : "");
        JsonNode j = om.readTree(clipArr(repl.run(sys, q.toString())));
        List<com.resq.dto.RankOut> out = new ArrayList<>();
        if (j.isArray()) {
            for (JsonNode n : j) {
                out.add(com.resq.dto.RankOut.builder()
                        .score(Math.max(0, Math.min(100, n.path("score").asInt(-1))))
                        .reason(n.path("reason").asText("").trim())
                        .build());
            }
        }
        return out;
    }

    private String clipArr(String s) {
        if (s == null) {
            return "[]";
        }
        int i = s.indexOf('[');
        int j = s.lastIndexOf(']');
        return i >= 0 && j > i ? s.substring(i, j + 1) : "[]";
    }

    public Est impact(com.resq.dom.Dona d) throws Exception {
        String sys = "You estimate the real impact of ONE delivered surplus donation. Reply with ONLY a JSON object "
                + "{\"kg\":number,\"meals\":integer,\"co2\":number,\"people\":integer}. meals=0 for non-food. "
                + "co2 is kg of CO2 emissions avoided. Be realistic and modest, no exaggeration.";
        String q = "Delivered donation: item=" + nv(d.getTitle()) + ", category=" + d.getCat()
                + ", quantity=" + nv(d.getQty()) + ".";
        JsonNode j = om.readTree(clip(repl.run(sys, q)));
        return Est.builder()
                .kg(Math.max(0, j.path("kg").asDouble(0)))
                .meals(Math.max(0, j.path("meals").asInt(0)))
                .co2(Math.max(0, j.path("co2").asDouble(0)))
                .people(Math.max(0, j.path("people").asInt(0)))
                .build();
    }

    private String ask(AnaIn in) {
        String img = in.getImg() != null && !in.getImg().isBlank()
                ? "A photo of the item is attached - use it to identify and assess the item.\n" : "";
        String cat = in.getClbl() != null && !in.getClbl().isBlank() ? in.getClbl() : String.valueOf(in.getCat());
        return img + "Donation to assess:\n"
                + "Category: " + cat + "\n"
                + "Quantity: " + nv(in.getQty()) + "\n"
                + "Storage: " + nv(in.getStore()) + "\n"
                + "Condition: " + nv(in.getCond()) + "\n"
                + "Pickup window: " + nv(in.getPwin()) + "\n"
                + "Area: " + nv(in.getArea()) + "\n"
                + "Info for AI: " + nv(in.getNotes());
    }

    private byte[] decode(String img) {
        String b = img.contains(",") ? img.substring(img.indexOf(',') + 1) : img;
        return java.util.Base64.getDecoder().decode(b);
    }

    private Safe safe(String s) {
        try {
            return Safe.valueOf(s.trim().toUpperCase());
        } catch (Exception e) {
            return Safe.REVIEW;
        }
    }

    private String clip(String s) {
        if (s == null) {
            return "{}";
        }
        int i = s.indexOf('{');
        int j = s.lastIndexOf('}');
        if (i >= 0 && j > i) {
            return s.substring(i, j + 1);
        }
        return "{}";
    }

    private String str(JsonNode j, String k, String def) {
        JsonNode v = j.path(k);
        return v.isMissingNode() || v.isNull() ? def : v.asText(def);
    }

    private List<String> arr(JsonNode j, String k) {
        List<String> l = new ArrayList<>();
        JsonNode v = j.path(k);
        if (v.isArray()) {
            for (JsonNode p : v) {
                String t = p.asText("").trim();
                if (!t.isBlank()) {
                    l.add(t);
                }
            }
        }
        return l;
    }

    private String nv(String v) {
        return v == null || v.isBlank() ? "not provided" : v.trim();
    }
}
