package com.resq.web;

import com.resq.dom.Reward;
import com.resq.repo.RewardR;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RewardC {

    private final RewardR repo;

    @GetMapping("/rewards")
    public List<Reward> all() {
        return repo.findAll();
    }
}
