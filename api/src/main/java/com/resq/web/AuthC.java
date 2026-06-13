package com.resq.web;

import com.resq.dom.Acct;
import com.resq.dto.AcctOut;
import com.resq.dto.LoginIn;
import com.resq.dto.RegIn;
import com.resq.svc.Auth;
import lombok.RequiredArgsConstructor;
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
public class AuthC {

    private final Auth auth;

    @PostMapping("/reg")
    public AcctOut reg(@RequestBody RegIn in) {
        return out(auth.reg(in));
    }

    @PostMapping("/login")
    public AcctOut login(@RequestBody LoginIn in) {
        return out(auth.login(in));
    }

    @PutMapping("/acct/{id}/prof")
    public AcctOut link(@PathVariable Long id, @RequestParam("val") Long prof) {
        return out(auth.link(id, prof));
    }

    private AcctOut out(Acct a) {
        return AcctOut.builder()
                .id(a.getId())
                .email(a.getEmail())
                .role(a.getRole())
                .name(a.getName())
                .area(a.getArea())
                .prof(a.getProf())
                .admin(a.getAdmin() != null && a.getAdmin())
                .build();
    }
}
