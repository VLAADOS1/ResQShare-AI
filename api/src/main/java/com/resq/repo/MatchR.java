package com.resq.repo;

import com.resq.dom.Match;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchR extends JpaRepository<Match, Long> {

    List<Match> findByDona(Long dona);
}
