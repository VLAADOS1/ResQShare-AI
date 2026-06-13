package com.resq.repo;

import com.resq.dom.Reward;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardR extends JpaRepository<Reward, Long> {

    Optional<Reward> findByTitle(String title);
}
