package com.resq.ai;

import org.springframework.stereotype.Component;

@Component
public class Demand {

    public String near(String area) {
        String a = area == null || area.isBlank() ? "your area" : area.trim();
        return "High demand near " + a + ": ready-to-eat meals, hygiene kits, baby items.";
    }
}
