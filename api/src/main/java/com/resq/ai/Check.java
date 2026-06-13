package com.resq.ai;

import com.resq.dto.AnaIn;
import com.resq.type.Cat;
import com.resq.type.Safe;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class Check {

    public Eval eval(AnaIn in) {
        boolean food = in.getCat() == Cat.FOOD;
        String cd = low(in.getCond());
        String st = low(in.getStore());
        String pw = low(in.getPwin());
        int r = food ? 12 : 6;

        if (cd.contains("opened")) {
            r += 30;
        }
        if (cd.contains("prepared")) {
            r += 22;
        }
        if (cd.contains("hot")) {
            r += 18;
        }
        if (cd.contains("used")) {
            r += 12;
        }
        if (cd.contains("repair")) {
            r += 26;
        }
        if (cd.contains("sealed") || cd.contains("new")) {
            r -= 8;
        }
        if (st.contains("room")) {
            r += 14;
        }
        if (st.contains("refrig") || st.contains("frozen")) {
            r -= 6;
        }
        if (pw.contains("tomorrow") || pw.contains("late")) {
            r += 10;
        }
        if (blank(in.getStore()) && food) {
            r += 12;
        }
        r = Math.max(4, Math.min(96, r));

        Safe s = grade(in, r, cd);
        return Eval.builder()
                .safe(s)
                .risk(r)
                .checks(food ? foods() : goods())
                .quests(quest(in))
                .expl(why(in, s, r))
                .build();
    }

    private Safe grade(AnaIn in, int r, String cd) {
        if (in.getCat() == Cat.CLOTH && cd.contains("repair")) {
            return Safe.RECYCLE;
        }
        boolean food = in.getCat() == Cat.FOOD;
        if (!food) {
            return r >= 50 ? Safe.REVIEW : Safe.SAFE;
        }
        if (r >= 70) {
            return Safe.UNSAFE;
        }
        if (r >= 52) {
            return Safe.REVIEW;
        }
        if (r >= 36) {
            return Safe.ORGONLY;
        }
        if (r >= 22) {
            return Safe.SOON;
        }
        return Safe.SAFE;
    }

    private List<String> foods() {
        List<String> l = new ArrayList<>();
        l.add("Packaging intact?");
        l.add("Allergen label visible?");
        l.add("Stored below 5 C if refrigerated?");
        l.add("No signs of spoilage?");
        l.add("Pickup within recommended time?");
        return l;
    }

    private List<String> goods() {
        List<String> l = new ArrayList<>();
        l.add("Clean?");
        l.add("No major damage?");
        l.add("Correct category?");
        l.add("Safe for reuse?");
        return l;
    }

    private List<String> quest(AnaIn in) {
        List<String> l = new ArrayList<>();
        String cd = low(in.getCond());
        if (blank(in.getStore()) && in.getCat() == Cat.FOOD) {
            l.add("Please confirm: was this stored refrigerated?");
        }
        if (!cd.contains("sealed") && in.getCat() == Cat.FOOD) {
            l.add("Please confirm: is the package sealed?");
        }
        return l;
    }

    private String why(AnaIn in, Safe s, int r) {
        String b = "Risk " + r + "/100. ";
        switch (s) {
            case SAFE:
                return b + "Item looks packaged and stable within pickup window.";
            case SOON:
                return b + "Perishable item, recommend pickup soon.";
            case ORGONLY:
                return b + "Prepared or perishable, route through organizations first.";
            case REVIEW:
                return b + "Storage or condition unclear, needs human review.";
            case UNSAFE:
                return b + "High risk signals, not safe to donate.";
            default:
                return b + "Not suitable for reuse, recycle recommended.";
        }
    }

    private String low(String v) {
        return v == null ? "" : v.toLowerCase();
    }

    private boolean blank(String v) {
        return v == null || v.isBlank();
    }
}
