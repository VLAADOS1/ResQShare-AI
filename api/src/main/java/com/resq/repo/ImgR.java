package com.resq.repo;

import com.resq.dom.Img;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgR extends JpaRepository<Img, Long> {

    List<Img> findByDona(Long dona);
}
