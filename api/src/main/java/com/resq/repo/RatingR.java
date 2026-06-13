package com.resq.repo;

import com.resq.dom.Rating;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingR extends JpaRepository<Rating, Long> {

    Optional<Rating> findByAcc(Long acc);
}
