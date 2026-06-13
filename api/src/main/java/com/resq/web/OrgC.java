package com.resq.web;

import com.resq.dom.Offer;
import com.resq.dom.Org;
import com.resq.repo.OrgR;
import com.resq.svc.OffS;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrgC {

    private final OrgR repo;
    private final OffS off;

    @GetMapping("/org")
    public List<Org> list() {
        return repo.findAll();
    }

    @GetMapping("/org/{id}/income")
    public List<Offer> income(@PathVariable Long id) {
        return off.income(id);
    }
}
