package com.resq.ai;

import com.resq.dto.AnaIn;
import com.resq.type.Cat;
import org.springframework.stereotype.Component;

@Component
public class Scan {

    public String item(AnaIn in) {
        String h = hint(in);
        if (in.getCat() == Cat.FOOD) {
            if (h.contains("sandwich")) {
                return "Packaged sandwiches";
            }
            if (h.contains("bread")) {
                return "Fresh bread";
            }
            if (h.contains("fruit")) {
                return "Fresh fruit";
            }
            if (h.contains("meal") || h.contains("rice")) {
                return "Prepared meals";
            }
            return "Packaged food";
        }
        if (in.getCat() == Cat.CLOTH) {
            if (h.contains("winter") || h.contains("coat")) {
                return "Winter clothes";
            }
            if (h.contains("child") || h.contains("kid")) {
                return "Children clothes";
            }
            return "Used clothes";
        }
        if (in.getCat() == Cat.HYG) {
            return "Hygiene kits";
        }
        if (in.getCat() == Cat.BABY) {
            return "Baby items";
        }
        if (in.getCat() == Cat.SCHOOL) {
            return "School supplies";
        }
        return "Donation items";
    }

    private String hint(AnaIn in) {
        StringBuilder b = new StringBuilder();
        if (in.getNotes() != null) {
            b.append(in.getNotes()).append(' ');
        }
        if (in.getQty() != null) {
            b.append(in.getQty()).append(' ');
        }
        if (in.getPhoto() != null) {
            b.append(in.getPhoto());
        }
        return b.toString().toLowerCase();
    }
}
