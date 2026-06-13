package com.resq.repo;

import com.resq.dom.Acct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcctR extends JpaRepository<Acct, Long> {

    Optional<Acct> findByEmail(String email);

    boolean existsByEmail(String email);
}
