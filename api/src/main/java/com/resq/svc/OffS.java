package com.resq.svc;

import com.resq.dom.Offer;
import com.resq.dto.OffIn;
import com.resq.repo.OfferR;
import com.resq.type.Stat;
import com.resq.type.Ttype;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OffS {

    private final OfferR repo;
    private final DonaS dona;

    public Offer send(OffIn in) {
        Offer o = Offer.builder()
                .dona(in.getDona())
                .tgt(in.getTgt())
                .ttype(in.getTtype())
                .stat(Stat.SENT)
                .build();
        dona.move(in.getDona(), Stat.SENT);
        return repo.save(o);
    }

    public List<Offer> income(Long org) {
        return repo.findByTgtAndTtype(org, Ttype.ORG);
    }

    public Offer accept(Long id) {
        Offer o = repo.findById(id).orElseThrow();
        o.setStat(Stat.MATCH);
        dona.move(o.getDona(), Stat.MATCH);
        return repo.save(o);
    }

    public Offer decline(Long id) {
        Offer o = repo.findById(id).orElseThrow();
        o.setStat(Stat.REJ);
        return repo.save(o);
    }
}
