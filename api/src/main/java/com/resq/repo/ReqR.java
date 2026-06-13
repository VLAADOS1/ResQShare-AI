package com.resq.repo;

import com.resq.dom.Req;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReqR extends JpaRepository<Req, Long> {

    List<Req> findByRecip(Long recip);
}
