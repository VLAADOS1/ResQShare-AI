package com.resq.repo;

import com.resq.dom.Msg;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsgR extends JpaRepository<Msg, Long> {

    List<Msg> findByFrmOrToOrderByTsAsc(Long frm, Long to);
}
