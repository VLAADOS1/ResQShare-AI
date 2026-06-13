package com.resq.repo;

import com.resq.dom.Stats;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatsR extends JpaRepository<Stats, Long> {

    Optional<Stats> findByAcc(Long acc);
}
