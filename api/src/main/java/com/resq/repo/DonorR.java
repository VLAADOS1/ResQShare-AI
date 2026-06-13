package com.resq.repo;

import com.resq.dom.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorR extends JpaRepository<Donor, Long> {
}
