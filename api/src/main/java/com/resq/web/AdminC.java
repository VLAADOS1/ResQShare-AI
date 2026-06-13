package com.resq.web;

import com.resq.dom.Acct;
import com.resq.dom.Dona;
import com.resq.dom.Reward;
import com.resq.dto.AcctOut;
import com.resq.dto.RewardIn;
import com.resq.repo.AcctR;
import com.resq.repo.DonaR;
import com.resq.repo.DonorR;
import com.resq.repo.OrgR;
import com.resq.repo.RecipR;
import com.resq.repo.RewardR;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminC {

    private final AcctR accts;
    private final DonaR donas;
    private final DonorR donors;
    private final RecipR recips;
    private final OrgR orgs;
    private final RewardR rewards;

    @GetMapping("/accts")
    public List<AcctOut> accts() {
        return accts.findAll().stream().map(this::out).toList();
    }

    @GetMapping("/donas")
    public List<Dona> donas() {
        return donas.findAll();
    }

    @GetMapping("/sum")
    public Map<String, Long> sum() {
        return Map.of(
                "accts", accts.count(),
                "donas", donas.count(),
                "donors", donors.count(),
                "recips", recips.count(),
                "orgs", orgs.count());
    }

    @DeleteMapping("/dona/{id}")
    public Map<String, Boolean> delDona(@PathVariable Long id) {
        donas.deleteById(id);
        return Map.of("ok", true);
    }

    @PutMapping("/acct/{id}/ban")
    public AcctOut ban(@PathVariable Long id, @RequestParam("val") boolean val) {
        Acct a = accts.findById(id).orElseThrow(() -> new IllegalStateException("No account"));
        a.setBanned(val);
        return out(accts.save(a));
    }

    @DeleteMapping("/acct/{id}")
    public Map<String, Boolean> delAcct(@PathVariable Long id) {
        accts.deleteById(id);
        return Map.of("ok", true);
    }

    @PostMapping("/reward")
    public Reward addReward(@RequestBody RewardIn in) {
        return rewards.save(Reward.builder()
                .title(in.getTitle())
                .emoji(in.getEmoji() == null || in.getEmoji().isBlank() ? "🏅" : in.getEmoji())
                .descr(in.getDescr())
                .metric(in.getMetric())
                .goal(in.getGoal() == null ? 1 : in.getGoal())
                .gift(in.getGift() == null || in.getGift().isBlank() ? null : in.getGift())
                .build());
    }

    @DeleteMapping("/reward/{id}")
    public Map<String, Boolean> delReward(@PathVariable Long id) {
        rewards.deleteById(id);
        return Map.of("ok", true);
    }

    private AcctOut out(Acct a) {
        return AcctOut.builder()
                .id(a.getId()).email(a.getEmail()).role(a.getRole())
                .name(a.getName()).area(a.getArea()).prof(a.getProf())
                .admin(a.getAdmin() != null && a.getAdmin())
                .banned(a.getBanned() != null && a.getBanned())
                .test(a.getTest() != null && a.getTest())
                .build();
    }
}
