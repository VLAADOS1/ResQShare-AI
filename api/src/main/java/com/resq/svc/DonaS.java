package com.resq.svc;

import com.resq.ai.AiS;
import com.resq.ai.Est;
import com.resq.dom.Dona;
import com.resq.dto.DonaIn;
import com.resq.repo.DonaR;
import com.resq.type.Safe;
import com.resq.type.Stat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DonaS {

    private final DonaR repo;
    private final com.resq.repo.ImgR imgs;
    private final AiS ai;
    private final StatsS stats;

    public Dona create(DonaIn in) {
        Stat st = in.getSafe() == Safe.REVIEW || in.getSafe() == Safe.UNSAFE
                ? Stat.REVIEW : Stat.AVAIL;
        java.util.List<byte[]> all = new java.util.ArrayList<>();
        if (in.getImgs() != null) {
            for (String s : in.getImgs()) {
                byte[] b = decode(s);
                if (b != null) {
                    all.add(b);
                }
            }
        }
        if (all.isEmpty()) {
            byte[] one = decode(in.getImg());
            if (one != null) {
                all.add(one);
            }
        }
        byte[] pic = all.isEmpty() ? null : all.get(0);
        Dona d = Dona.builder()
                .donor(in.getDonor())
                .acc(in.getAcc())
                .dname(in.getDname())
                .lat(in.getLat())
                .lng(in.getLng())
                .img(pic)
                .pic(pic != null)
                .title(in.getTitle())
                .descr(in.getDescr())
                .cat(in.getCat())
                .clbl(in.getClbl())
                .qty(in.getQty())
                .store(in.getStore())
                .cond(in.getCond())
                .pwin(in.getPwin())
                .area(in.getArea())
                .notes(in.getNotes())
                .photo(in.getPhoto())
                .safe(in.getSafe())
                .risk(in.getRisk())
                .expl(in.getExpl())
                .chk(in.getChk())
                .deliv(in.getDeliv())
                .rad(in.getRad())
                .test(in.getTest() != null && in.getTest())
                .stat(st)
                .build();
        Dona out = repo.save(d);
        for (byte[] b : all) {
            imgs.save(com.resq.dom.Img.builder().dona(out.getId()).data(b).build());
        }
        return out;
    }

    public byte[] pic(Long id) {
        return get(id).getImg();
    }

    public List<Long> imgIds(Long dona) {
        return imgs.findByDona(dona).stream().map(com.resq.dom.Img::getId).toList();
    }

    public byte[] imgBytes(Long iid) {
        com.resq.dom.Img i = imgs.findById(iid).orElse(null);
        return i == null ? null : i.getData();
    }

    private byte[] decode(String img) {
        if (img == null || img.isBlank()) {
            return null;
        }
        String b = img.contains(",") ? img.substring(img.indexOf(',') + 1) : img;
        try {
            return java.util.Base64.getDecoder().decode(b);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Dona> all() {
        return repo.findAll();
    }

    public List<Dona> byStat(Stat s) {
        return repo.findByStat(s);
    }

    public List<Dona> byDonor(Long donor) {
        return repo.findByDonor(donor);
    }

    public Dona get(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public com.resq.dto.MoveOut done(Long id) {
        Dona d = get(id);
        d.setStat(Stat.DONE);
        Dona out = repo.save(d);
        Est e = ai.impact(out);
        stats.credit(out.getAcc(), e);
        return com.resq.dto.MoveOut.builder().dona(out).gain(e).build();
    }

    public Dona move(Long id, Stat s) {
        Dona d = get(id);
        d.setStat(s);
        Dona out = repo.save(d);
        if (s == Stat.DONE) {
            Est e = ai.impact(out);
            stats.credit(out.getAcc(), e);
        }
        return out;
    }
}
