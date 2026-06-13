package com.resq.web;

import com.resq.dom.Acct;
import com.resq.dom.Donor;
import com.resq.dom.Org;
import com.resq.dom.Recip;
import com.resq.repo.AcctR;
import com.resq.repo.DonorR;
import com.resq.repo.OrgR;
import com.resq.repo.RecipR;
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
public class ProfC {

    private final DonorR donors;
    private final RecipR recips;
    private final OrgR orgs;
    private final AcctR accts;

    @PostMapping("/donor")
    public Donor donor(@RequestBody Donor b) {
        return donors.save(b);
    }

    @PostMapping("/recip")
    public Recip recip(@RequestBody Recip b) {
        return recips.save(b);
    }

    @PostMapping("/org")
    public Org org(@RequestBody Org b) {
        return orgs.save(b);
    }

    @GetMapping("/donor/{id}")
    public Donor one(@PathVariable Long id) {
        return donors.findById(id).orElse(null);
    }

    @PutMapping("/donor/{id}/bio")
    public Donor bio(@PathVariable Long id, @RequestParam("val") String val) {
        Donor d = donors.findById(id).orElseThrow();
        d.setBio(val);
        return donors.save(d);
    }

    @PutMapping("/acct/{id}/name")
    public Acct name(@PathVariable Long id, @RequestParam("val") String val) {
        Acct a = accts.findById(id).orElseThrow();
        a.setName(val);
        return accts.save(a);
    }
}
