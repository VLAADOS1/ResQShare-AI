package com.resq.web;

import com.resq.dom.Offer;
import com.resq.dto.OffIn;
import com.resq.svc.OffS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OffC {

    private final OffS svc;

    @PostMapping("/offer")
    public Offer send(@RequestBody OffIn in) {
        return svc.send(in);
    }

    @PutMapping("/offer/{id}/accept")
    public Offer accept(@PathVariable Long id) {
        return svc.accept(id);
    }

    @PutMapping("/offer/{id}/decline")
    public Offer decline(@PathVariable Long id) {
        return svc.decline(id);
    }
}
