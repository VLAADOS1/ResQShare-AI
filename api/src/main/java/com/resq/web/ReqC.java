package com.resq.web;

import com.resq.dom.Req;
import com.resq.dto.ReqIn;
import com.resq.svc.ReqS;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReqC {

    private final ReqS svc;

    @PostMapping("/req")
    public Req create(@RequestBody ReqIn in) {
        return svc.create(in);
    }

    @PostMapping("/req/draft")
    public java.util.Map<String, String> draft(@RequestBody ReqIn in) {
        return java.util.Map.of("pub", svc.draft(in));
    }

    @org.springframework.web.bind.annotation.PutMapping("/req/{id}/fulfil")
    public com.resq.ai.Est fulfil(@org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam("acc") Long acc) {
        return svc.fulfil(id, acc);
    }

    @GetMapping("/req")
    public List<Req> list() {
        return svc.all();
    }

    @org.springframework.web.bind.annotation.PutMapping("/req/{id}/stat")
    public Req stat(@org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestParam("val") String val) {
        return svc.setStat(id, val);
    }
}
