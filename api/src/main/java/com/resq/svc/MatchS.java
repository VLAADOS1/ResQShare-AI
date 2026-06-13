package com.resq.svc;

import com.resq.ai.AiS;
import com.resq.dom.Acct;
import com.resq.dom.Dona;
import com.resq.dom.Org;
import com.resq.dom.Recip;
import com.resq.dto.MchOut;
import com.resq.repo.AcctR;
import com.resq.repo.OrgR;
import com.resq.repo.RecipR;
import com.resq.type.Cat;
import com.resq.type.Safe;
import com.resq.type.Ttype;
import com.resq.type.Urg;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchS {

    private static final double MAX_KM = 60.0;

    private final OrgR orgs;
    private final RecipR recips;
    private final AcctR accts;
    private final AiS ai;

    public List<MchOut> top(Dona d, String lang) {
        boolean org1 = perish(d);
        List<MchOut> all = new ArrayList<>();
        for (Org o : orgs.findAll()) {
            if (o.getTest() != null && o.getTest()) {
                continue;
            }
            all.add(forOrg(d, o, org1));
        }
        for (Recip r : recips.findAll()) {
            if (r.getTest() != null && r.getTest()) {
                continue;
            }
            all.add(forRecip(d, r, org1));
        }
        all.removeIf(m -> m.getDist() == null || m.getDist() > MAX_KM);
        all.sort(Comparator.comparingInt(MchOut::getScore).reversed());
        List<MchOut> pool = all.size() > 8 ? new ArrayList<>(all.subList(0, 8)) : all;
        boolean ru = "IT".equalsIgnoreCase(lang);
        if (!pool.isEmpty()) {
            List<String> lines = new ArrayList<>();
            for (MchOut m : pool) {
                lines.add(line(m));
            }
            List<com.resq.dto.RankOut> ranks = ai.rank(item(d), String.valueOf(d.getCat()), info(d), lines, ru);
            if (ranks.size() == pool.size()) {
                for (int i = 0; i < pool.size(); i++) {
                    com.resq.dto.RankOut r = ranks.get(i);
                    if (r.getScore() != null && r.getScore() >= 0) {
                        pool.get(i).setScore(clamp(r.getScore()));
                    }
                    if (r.getReason() != null && !r.getReason().isBlank()) {
                        pool.get(i).setReason(r.getReason());
                    }
                }
                pool.sort(Comparator.comparingInt(MchOut::getScore).reversed());
            } else {
                List<String> why = ai.why(item(d), String.valueOf(d.getCat()), lines, ru);
                if (why.size() == pool.size()) {
                    for (int i = 0; i < pool.size(); i++) {
                        if (!why.get(i).isBlank()) {
                            pool.get(i).setReason(why.get(i));
                        }
                    }
                }
            }
        }
        return pool.size() > 5 ? new ArrayList<>(pool.subList(0, 5)) : pool;
    }

    private String info(Dona d) {
        StringBuilder b = new StringBuilder();
        if (d.getQty() != null && !d.getQty().isBlank()) {
            b.append("quantity ").append(d.getQty().trim()).append("; ");
        }
        if (d.getCond() != null && !d.getCond().isBlank()) {
            b.append("condition ").append(d.getCond().trim()).append("; ");
        }
        if (d.getSafe() != null) {
            b.append("safety ").append(d.getSafe());
            if (perish(d)) {
                b.append(" (perishable/route via organizations)");
            }
        }
        return b.toString();
    }

    private String item(Dona d) {
        return d.getTitle() == null || d.getTitle().isBlank() ? String.valueOf(d.getCat()) : d.getTitle();
    }

    private String line(MchOut m) {
        StringBuilder b = new StringBuilder(m.getName());
        if (m.getKind() != null) {
            b.append(" (").append(m.getKind()).append(")");
        }
        if (m.getArea() != null) {
            b.append(", ").append(m.getArea());
        }
        if (m.getDist() != null) {
            b.append(", ").append(m.getDist()).append(" km away");
        }
        if (m.getTtype() == Ttype.RECIP && m.getNeed() != null) {
            b.append(", needs ").append(m.getNeed());
        }
        if (m.isUrgent()) {
            b.append(", urgent");
        }
        if (m.getTtype() == Ttype.ORG && m.isAccepts()) {
            b.append(", accepts this category");
        }
        return b.toString();
    }

    private MchOut forOrg(Dona d, Org o, boolean org1) {
        int s = 60;
        boolean fit = accept(o, d.getCat());
        if (fit) {
            s += 20;
        }
        if (org1) {
            s += 12;
        }
        if (o.getCap() != null && o.getCap() > 10) {
            s += 4;
        }
        Double km = dist(d.getLat(), d.getLng(), o.getLat(), o.getLng());
        if (km != null) {
            s -= (int) Math.min(22, km / 3.0);
        }
        return MchOut.builder()
                .tgt(o.getId()).acc(o.getAcc()).ttype(Ttype.ORG)
                .name(o.getName() == null ? "Organization" : o.getName())
                .dist(km).score(clamp(s))
                .kind(o.getOtype()).area(o.getArea()).accepts(fit)
                .test(o.getTest() != null && o.getTest()).build();
    }

    private MchOut forRecip(Dona d, Recip r, boolean org1) {
        int s = 58;
        boolean fit = needs(r, d);
        if (fit) {
            s += 22;
        }
        if (org1) {
            s -= 10;
        }
        s += urg(r.getUrg());
        Double km = dist(d.getLat(), d.getLng(), r.getLat(), r.getLng());
        if (km != null) {
            s -= (int) Math.min(22, km / 3.0);
            if (r.getRadius() != null && km <= r.getRadius()) {
                s += 6;
            }
        }
        return MchOut.builder()
                .tgt(r.getId()).acc(r.getAcc()).ttype(Ttype.RECIP)
                .name(name(r)).dist(km).score(clamp(s))
                .kind(r.getAtype()).area(r.getArea()).need(r.getNeeds())
                .urgent(r.getUrg() == Urg.HIGH)
                .test(r.getTest() != null && r.getTest()).build();
    }

    private String name(Recip r) {
        if (r.getAcc() != null) {
            Acct a = accts.findById(r.getAcc()).orElse(null);
            if (a != null && a.getName() != null && !a.getName().isBlank()) {
                return a.getName();
            }
        }
        if (r.getAtype() != null && !r.getAtype().isBlank()) {
            return r.getArea() == null ? r.getAtype() : r.getAtype() + " - " + r.getArea();
        }
        return "Recipient";
    }

    private int urg(Urg u) {
        if (u == Urg.HIGH) {
            return 8;
        }
        if (u == Urg.MED) {
            return 3;
        }
        return 0;
    }

    private boolean perish(Dona d) {
        Safe s = d.getSafe();
        return d.getCat() == Cat.FOOD && (s == Safe.SOON || s == Safe.ORGONLY || s == Safe.REVIEW);
    }

    private boolean accept(Org o, Cat c) {
        return o.getAccept() != null && c != null && o.getAccept().toLowerCase().contains(word(c));
    }

    private boolean needs(Recip r, Dona d) {
        return r.getNeeds() != null && d.getCat() != null
                && r.getNeeds().toLowerCase().contains(word(d.getCat()));
    }

    private String word(Cat c) {
        return switch (c) {
            case FOOD -> "food";
            case CLOTH -> "cloth";
            case HYG -> "hyg";
            case BABY -> "baby";
            case SCHOOL -> "school";
            default -> "other";
        };
    }

    private Double dist(Double la1, Double ln1, Double la2, Double ln2) {
        if (la1 == null || ln1 == null || la2 == null || ln2 == null) {
            return null;
        }
        double dLa = Math.toRadians(la2 - la1);
        double dLn = Math.toRadians(ln2 - ln1);
        double a = Math.sin(dLa / 2) * Math.sin(dLa / 2)
                + Math.cos(Math.toRadians(la1)) * Math.cos(Math.toRadians(la2)) * Math.sin(dLn / 2) * Math.sin(dLn / 2);
        double km = 6371.0 * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return Math.round(km * 10) / 10.0;
    }

    private int clamp(int s) {
        return Math.max(35, Math.min(99, s));
    }
}
