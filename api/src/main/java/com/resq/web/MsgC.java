package com.resq.web;

import com.resq.dom.Msg;
import com.resq.dto.MsgIn;
import com.resq.dto.ThrdOut;
import com.resq.svc.MsgS;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MsgC {

    private final MsgS svc;

    @PostMapping("/msg")
    public Msg send(@RequestBody MsgIn in) {
        return svc.send(in);
    }

    @GetMapping("/msg")
    public List<Msg> conv(@RequestParam Long acc, @RequestParam Long other,
            @RequestParam(required = false) Long dona) {
        return svc.conv(acc, other, dona);
    }

    @GetMapping("/threads")
    public List<ThrdOut> threads(@RequestParam Long acc) {
        return svc.threads(acc);
    }
}
