package com.resq.web;

import com.resq.ai.AiS;
import com.resq.dom.Dona;
import com.resq.dto.AnaIn;
import com.resq.dto.AnaOut;
import com.resq.dto.DonaIn;
import com.resq.dto.MchOut;
import com.resq.svc.DonaS;
import com.resq.svc.MatchS;
import com.resq.type.Stat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DonaC {

    private final AiS ai;
    private final DonaS svc;
    private final MatchS mch;

    @PostMapping("/ana")
    public AnaOut ana(@RequestBody AnaIn in) {
        return ai.ana(in);
    }

    @PostMapping("/scan")
    public com.resq.dto.ScanOut scan(@RequestBody com.resq.dto.ScanIn in) {
        return ai.scan(in);
    }

    @PostMapping("/dona")
    public Dona create(@RequestBody DonaIn in) {
        return svc.create(in);
    }

    @GetMapping("/dona")
    public List<Dona> list(@RequestParam(required = false) Long donor) {
        return donor == null ? svc.all() : svc.byDonor(donor);
    }

    @GetMapping("/dona/{id}")
    public Dona one(@PathVariable Long id) {
        return svc.get(id);
    }

    @GetMapping(value = "/dona/{id}/img", produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
    public byte[] pic(@PathVariable Long id) {
        return svc.pic(id);
    }

    @GetMapping("/dona/{id}/imgs")
    public List<Long> imgs(@PathVariable Long id) {
        return svc.imgIds(id);
    }

    @GetMapping(value = "/img/{iid}", produces = org.springframework.http.MediaType.IMAGE_JPEG_VALUE)
    public byte[] img(@PathVariable Long iid) {
        return svc.imgBytes(iid);
    }

    @GetMapping("/dona/{id}/match")
    public List<MchOut> match(@PathVariable Long id, @RequestParam(required = false) String lang) {
        return mch.top(svc.get(id), lang);
    }

    @PostMapping("/match")
    public List<MchOut> preview(@RequestBody DonaIn in, @RequestParam(required = false) String lang) {
        Dona d = Dona.builder()
                .lat(in.getLat()).lng(in.getLng()).cat(in.getCat())
                .safe(in.getSafe()).title(in.getTitle()).build();
        return mch.top(d, lang);
    }

    @PutMapping("/dona/{id}/stat")
    public Dona move(@PathVariable Long id, @RequestParam Stat val) {
        return svc.move(id, val);
    }

    @PutMapping("/dona/{id}/done")
    public com.resq.dto.MoveOut done(@PathVariable Long id) {
        return svc.done(id);
    }

    @GetMapping("/demand")
    public String demand(@RequestParam(required = false) String area,
            @RequestParam(required = false) String lang) {
        return ai.demand(area, lang);
    }
}
