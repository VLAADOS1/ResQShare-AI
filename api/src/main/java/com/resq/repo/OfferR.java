package com.resq.repo;

import com.resq.dom.Offer;
import com.resq.type.Ttype;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferR extends JpaRepository<Offer, Long> {

    List<Offer> findByDona(Long dona);

    List<Offer> findByTgtAndTtype(Long tgt, Ttype ttype);
}
