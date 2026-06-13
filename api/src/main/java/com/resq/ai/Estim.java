package com.resq.ai;

import com.resq.dom.Dona;
import com.resq.type.Cat;
import org.springframework.stereotype.Component;

@Component
public class Estim {

    public Est calc(Dona d) {
        double kg = weight(d);
        int meals = d.getCat() == Cat.FOOD ? (int) Math.round(kg * 2.2) : 0;
        double co2 = Math.round(kg * 2.5 * 10) / 10.0;
        int ppl = Math.max(1, meals > 0 ? meals / 3 : 2);
        return Est.builder()
                .kg(kg)
                .meals(meals)
                .co2(co2)
                .people(ppl)
                .build();
    }

    private double weight(Dona d) {
        String q = d.getQty() == null ? "" : d.getQty().toLowerCase();
        double n = num(q);
        if (q.contains("kg")) {
            return n > 0 ? n : 4;
        }
        if (d.getCat() == Cat.FOOD) {
            return n > 0 ? Math.round(n * 0.3 * 10) / 10.0 : 3;
        }
        return n > 0 ? n : 3;
    }

    private double num(String q) {
        StringBuilder b = new StringBuilder();
        for (char c : q.toCharArray()) {
            if (Character.isDigit(c) || c == '.') {
                b.append(c);
            } else if (b.length() > 0) {
                break;
            }
        }
        try {
            return b.length() == 0 ? 0 : Double.parseDouble(b.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
