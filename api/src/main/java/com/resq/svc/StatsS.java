package com.resq.svc;

import com.resq.ai.Est;
import com.resq.dom.Stats;
import com.resq.repo.StatsR;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsS {

    private final StatsR repo;

    public Stats get() {
        return repo.findByAcc(0L).orElseGet(this::global);
    }

    public Stats view(Long acc) {
        return repo.findByAcc(acc).orElseGet(() -> blank(acc));
    }

    public Stats add(Est e) {
        return bump(get(), e);
    }

    public void credit(Long acc, Est e) {
        bump(get(), e);
        if (acc != null && acc > 0) {
            bump(repo.findByAcc(acc).orElseGet(() -> seed(acc)), e);
        }
    }

    private Stats global() {
        Stats legacy = repo.findAll().stream()
                .filter(s -> s.getAcc() == null)
                .max(Comparator.comparingInt(s -> s.getMeals() == null ? 0 : s.getMeals()))
                .orElse(null);
        if (legacy != null) {
            legacy.setAcc(0L);
            return repo.save(legacy);
        }
        return seed(0L);
    }

    private Stats bump(Stats s, Est e) {
        s.setKg(round(s.getKg() + e.getKg()));
        s.setMeals(s.getMeals() + e.getMeals());
        s.setCo2(round(s.getCo2() + e.getCo2()));
        s.setPeople(s.getPeople() + e.getPeople());
        s.setItems(s.getItems() + 1);
        return repo.save(s);
    }

    private Stats seed(Long acc) {
        return repo.save(blank(acc));
    }

    private Stats blank(Long acc) {
        return Stats.builder()
                .acc(acc).kg(0.0).meals(0).co2(0.0).items(0).people(0).orgs(0)
                .build();
    }

    private double round(double v) {
        return Math.round(v * 10) / 10.0;
    }
}
