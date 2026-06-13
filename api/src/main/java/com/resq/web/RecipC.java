package com.resq.web;

import com.resq.dom.Dona;
import com.resq.svc.DonaS;
import com.resq.type.Cat;
import com.resq.type.Safe;
import com.resq.type.Stat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipC {

    private final DonaS svc;

    @GetMapping("/feed")
    public List<Dona> feed(@RequestParam(required = false) Cat cat) {
        return svc.byStat(Stat.AVAIL).stream()
                .filter(d -> d.getTest() == null || !d.getTest())
                .filter(d -> d.getSafe() == Safe.SAFE || d.getSafe() == Safe.SOON)
                .filter(d -> cat == null || d.getCat() == cat)
                .toList();
    }
}
