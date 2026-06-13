package com.resq.svc;

import com.resq.dom.Msg;
import com.resq.dto.MsgIn;
import com.resq.dto.ThrdOut;
import com.resq.repo.MsgR;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MsgS {

    private final MsgR repo;

    public Msg send(MsgIn in) {
        return repo.save(Msg.builder()
                .dona(in.getDona())
                .frm(in.getFrm())
                .to(in.getTo())
                .nameFrm(in.getNameFrm())
                .nameTo(in.getNameTo())
                .body(in.getBody())
                .ts(System.currentTimeMillis())
                .build());
    }

    public List<Msg> conv(Long acc, Long other, Long dona) {
        List<Msg> all = repo.findByFrmOrToOrderByTsAsc(acc, acc);
        List<Msg> out = new ArrayList<>();
        for (Msg m : all) {
            boolean pair = (m.getFrm().equals(acc) && m.getTo().equals(other))
                    || (m.getFrm().equals(other) && m.getTo().equals(acc));
            boolean sameDona = dona == null || dona.equals(m.getDona());
            if (pair && sameDona) {
                out.add(m);
            }
        }
        return out;
    }

    public List<ThrdOut> threads(Long acc) {
        List<Msg> all = repo.findByFrmOrToOrderByTsAsc(acc, acc);
        Map<String, ThrdOut> m = new LinkedHashMap<>();
        for (Msg x : all) {
            boolean mine = x.getFrm().equals(acc);
            Long other = mine ? x.getTo() : x.getFrm();
            String oname = mine ? x.getNameTo() : x.getNameFrm();
            String key = other + ":" + x.getDona();
            m.put(key, ThrdOut.builder()
                    .other(other).oname(oname).dona(x.getDona())
                    .last(x.getBody()).lastFrm(x.getFrm()).ts(x.getTs()).build());
        }
        List<ThrdOut> out = new ArrayList<>(m.values());
        out.sort((a, b) -> Long.compare(b.getTs() == null ? 0 : b.getTs(), a.getTs() == null ? 0 : a.getTs()));
        return out;
    }
}
