package com.resq.repo;

import com.resq.dom.Dona;
import com.resq.type.Cat;
import com.resq.type.Stat;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonaR extends JpaRepository<Dona, Long> {

    List<Dona> findByStat(Stat stat);

    List<Dona> findByDonor(Long donor);

    List<Dona> findByCat(Cat cat);
}
