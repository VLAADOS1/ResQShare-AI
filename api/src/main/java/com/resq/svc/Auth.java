package com.resq.svc;

import com.resq.dom.Acct;
import com.resq.dto.LoginIn;
import com.resq.dto.RegIn;
import com.resq.repo.AcctR;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Auth {

    private final AcctR repo;
    private final PasswordEncoder enc;

    public Acct reg(RegIn in) {
        String mail = norm(in.getEmail());
        if (mail.isBlank() || in.getPass() == null || in.getPass().isBlank()) {
            throw new IllegalStateException("Email and password required");
        }
        if (!mail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalStateException("Invalid email address");
        }
        if (in.getPass().length() < 6) {
            throw new IllegalStateException("Password must be at least 6 characters");
        }
        if (repo.existsByEmail(mail)) {
            throw new IllegalStateException("Email already registered");
        }
        return repo.save(Acct.builder()
                .email(mail)
                .pass(enc.encode(in.getPass()))
                .role(in.getRole())
                .name(in.getName())
                .area(in.getArea())
                .build());
    }

    public Acct login(LoginIn in) {
        Acct a = repo.findByEmail(norm(in.getEmail()))
                .orElseThrow(() -> new IllegalStateException("No account found"));
        if (!enc.matches(in.getPass(), a.getPass())) {
            throw new IllegalStateException("Wrong password");
        }
        if (a.getBanned() != null && a.getBanned()) {
            throw new IllegalStateException("This account is banned");
        }
        return a;
    }

    public Acct link(Long id, Long prof) {
        Acct a = repo.findById(id).orElseThrow(() -> new IllegalStateException("No account"));
        a.setProf(prof);
        return repo.save(a);
    }

    private String norm(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
