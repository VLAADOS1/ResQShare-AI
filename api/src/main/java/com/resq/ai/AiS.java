package com.resq.ai;

import com.resq.dto.AnaIn;
import com.resq.dto.AnaOut;
import com.resq.type.Safe;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiS {

    private static final Logger log = LoggerFactory.getLogger(AiS.class);

    private final Scan scan;
    private final Lister list;
    private final Check chk;
    private final Demand dem;
    private final Estim estim;
    private final Brain brain;

    public AnaOut ana(AnaIn in) {
        if (brain.ready()) {
            try {
                return brain.ana(in);
            } catch (Exception e) {
                log.warn("AI ana fallback: {}", e.getMessage());
            }
        }
        return quick(in);
    }

    public AnaOut quick(AnaIn in) {
        String it = scan.item(in);
        Eval e = chk.eval(in);
        return AnaOut.builder()
                .item(it)
                .title(list.title(it, in))
                .descr(list.descr(it, in))
                .cat(in.getCat())
                .safe(e.getSafe())
                .risk(e.getRisk())
                .expl(e.getExpl())
                .checks(e.getChecks())
                .quests(e.getQuests())
                .recs(recs(e.getSafe()))
                .build();
    }

    public Est impact(com.resq.dom.Dona d) {
        if (brain.ready()) {
            try {
                Est e = brain.impact(d);
                if (e != null && e.getMeals() != null) {
                    return e;
                }
            } catch (Exception ex) {
                log.warn("AI impact fallback: {}", ex.getMessage());
            }
        }
        return estim.calc(d);
    }

    public List<String> why(String item, String cat, List<String> cand, boolean ru) {
        if (brain.ready() && !cand.isEmpty()) {
            try {
                List<String> r = brain.why(item, cat, cand, ru);
                if (r.size() == cand.size()) {
                    return r;
                }
            } catch (Exception e) {
                log.warn("AI why fallback: {}", e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    public com.resq.dto.ScanOut scan(com.resq.dto.ScanIn in) {
        if (brain.ready()) {
            try {
                com.resq.dto.ScanOut s = brain.scan(in);
                if (s != null) {
                    return s;
                }
            } catch (Exception e) {
                log.warn("AI scan fallback: {}", e.getMessage());
            }
        }
        return com.resq.dto.ScanOut.builder()
                .item("Donation items").clbl("").cat("OTHER").qty("").cond("").note("")
                .build();
    }

    public String reqText(com.resq.dom.Req r, String lang) {
        if (brain.ready()) {
            try {
                String t = brain.req(r, lang);
                if (t != null && !t.isBlank()) {
                    return t;
                }
            } catch (Exception e) {
                log.warn("AI req fallback: {}", e.getMessage());
            }
        }
        return basic(r);
    }

    private String basic(com.resq.dom.Req r) {
        String cat = r.getCat() == null ? "Support" : cap(r.getCat().name());
        StringBuilder b = new StringBuilder(cat).append(" needed");
        if (r.getQty() != null && !r.getQty().isBlank()) {
            b.append(" for ").append(r.getQty().trim());
        }
        b.append('.');
        if (r.getDescr() != null && !r.getDescr().isBlank()) {
            b.append(' ').append(r.getDescr().trim());
        }
        return b.toString();
    }

    private String cap(String v) {
        return v.isEmpty() ? v : v.charAt(0) + v.substring(1).toLowerCase();
    }

    public List<com.resq.dto.RankOut> rank(String item, String cat, String info, List<String> cand, boolean ru) {
        if (brain.ready() && !cand.isEmpty()) {
            try {
                List<com.resq.dto.RankOut> r = brain.rank(item, cat, info, cand, ru);
                if (r.size() == cand.size()) {
                    return r;
                }
            } catch (Exception e) {
                log.warn("AI rank fallback: {}", e.getMessage());
            }
        }
        return new ArrayList<>();
    }

    public String demand(String area, String lang) {
        if (brain.ready()) {
            try {
                return brain.demand(area, lang);
            } catch (Exception e) {
                log.warn("AI demand fallback: {}", e.getMessage());
            }
        }
        return dem.near(area);
    }

    private List<String> recs(Safe s) {
        List<String> l = new ArrayList<>();
        if (s == Safe.ORGONLY || s == Safe.SOON || s == Safe.REVIEW) {
            l.add("Local shelters");
            l.add("Food banks");
            l.add("Community centers");
        } else if (s == Safe.RECYCLE || s == Safe.UNSAFE) {
            l.add("Recycling points");
        } else {
            l.add("Nearby families");
            l.add("Students");
            l.add("Local organizations");
        }
        return l;
    }
}
