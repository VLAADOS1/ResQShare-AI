package com.resq.svc;

import com.resq.ai.AiS;
import com.resq.ai.Est;
import com.resq.dom.Dona;
import com.resq.dom.Recip;
import com.resq.dom.Req;
import com.resq.dto.ReqIn;
import com.resq.repo.RecipR;
import com.resq.repo.ReqR;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReqS {

    private final ReqR repo;
    private final RecipR recps;
    private final AiS ai;
    private final StatsS stats;

    public Req create(ReqIn in) {
        String pub = in.getPub() != null && !in.getPub().isBlank() ? in.getPub().trim() : anon(in);
        Req r = Req.builder()
                .recip(in.getRecip())
                .acc(in.getAcc())
                .name(in.getName())
                .lat(in.getLat())
                .lng(in.getLng())
                .area(in.getArea())
                .test(in.getTest() != null && in.getTest())
                .stat("OPEN")
                .cat(in.getCat())
                .descr(in.getDescr())
                .qty(in.getQty())
                .urg(in.getUrg())
                .radius(in.getRadius())
                .avail(in.getAvail())
                .restr(in.getRestr())
                .pub(pub)
                .build();
        return repo.save(r);
    }

    public String draft(ReqIn in) {
        Req r = Req.builder()
                .cat(in.getCat()).descr(in.getDescr()).qty(in.getQty())
                .urg(in.getUrg()).avail(in.getAvail()).restr(in.getRestr())
                .build();
        return ai.reqText(r, in.getLang());
    }

    public List<Req> all() {
        return repo.findAll();
    }

    public Req get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Req setStat(Long id, String stat) {
        Req r = get(id);
        r.setStat(stat);
        return repo.save(r);
    }

    public Est fulfil(Long id, Long acc) {
        Req r = get(id);
        r.setStat("DONE");
        repo.save(r);
        String item = r.getPub() != null && !r.getPub().isBlank() ? r.getPub() : r.getDescr();
        Dona t = Dona.builder().title(item).cat(r.getCat()).qty(r.getQty()).build();
        Est e = ai.impact(t);
        stats.credit(acc, e);
        return e;
    }

    private String anon(ReqIn in) {
        String who = "a person";
        if (in.getRecip() != null) {
            Recip r = recps.findById(in.getRecip()).orElse(null);
            if (r != null && r.getAtype() != null) {
                who = r.getAtype().toLowerCase();
            }
        }
        StringBuilder b = new StringBuilder();
        b.append(label(in)).append(" needed for ").append(who).append('.');
        if (in.getAvail() != null && !in.getAvail().isBlank()) {
            b.append(" Available ").append(in.getAvail().trim()).append('.');
        }
        if (in.getRestr() != null && !in.getRestr().isBlank()) {
            b.append(" Notes: ").append(in.getRestr().trim()).append('.');
        }
        return b.toString();
    }

    private String label(ReqIn in) {
        return in.getCat() == null ? "Support" : cap(in.getCat().name());
    }

    private String cap(String v) {
        return v.charAt(0) + v.substring(1).toLowerCase();
    }
}
