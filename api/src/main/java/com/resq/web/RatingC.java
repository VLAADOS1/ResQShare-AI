package com.resq.web;

import com.resq.dom.Rating;
import com.resq.repo.RatingR;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RatingC {

    private final RatingR repo;

    @PostMapping("/rate")
    public Rating add(@RequestBody Rating in) {
        if (in.getAcc() != null && in.getAcc() > 0) {
            Rating ex = repo.findByAcc(in.getAcc()).orElse(null);
            if (ex != null) {
                ex.setStars(in.getStars());
                ex.setTxt(in.getTxt());
                ex.setName(in.getName());
                return repo.save(ex);
            }
        }
        return repo.save(in);
    }

    @GetMapping("/rate")
    public List<Rating> all() {
        return repo.findAll();
    }

    @GetMapping("/rate/sum")
    public Map<String, Object> sum() {
        List<Rating> all = repo.findAll();
        double avg = all.isEmpty() ? 0
                : all.stream().mapToInt(r -> r.getStars() == null ? 0 : r.getStars()).average().orElse(0);
        return Map.of("avg", Math.round(avg * 10) / 10.0, "count", all.size());
    }
}
