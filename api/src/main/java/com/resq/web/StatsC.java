package com.resq.web;

import com.resq.dom.Stats;
import com.resq.svc.StatsS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StatsC {

    private final StatsS svc;

    @GetMapping("/stats")
    public Stats get() {
        return svc.get();
    }

    @GetMapping("/stats/{acc}")
    public Stats view(@org.springframework.web.bind.annotation.PathVariable Long acc) {
        return svc.view(acc);
    }
}
