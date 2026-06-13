package com.resq.ai;

import com.resq.dto.AnaIn;
import org.springframework.stereotype.Component;

@Component
public class Lister {

    public String title(String item, AnaIn in) {
        if (in.getQty() != null && !in.getQty().isBlank()) {
            return item + " (" + in.getQty().trim() + ")";
        }
        return item;
    }

    public String descr(String item, AnaIn in) {
        StringBuilder b = new StringBuilder();
        b.append(item).append(" available for donation");
        if (in.getArea() != null && !in.getArea().isBlank()) {
            b.append(" in ").append(in.getArea().trim());
        }
        b.append('.');
        if (in.getPwin() != null && !in.getPwin().isBlank()) {
            b.append(" Pickup ").append(in.getPwin().trim()).append('.');
        }
        if (in.getCond() != null && !in.getCond().isBlank()) {
            b.append(" Condition: ").append(in.getCond().trim()).append('.');
        }
        return b.toString();
    }
}
