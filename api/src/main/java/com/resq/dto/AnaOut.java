package com.resq.dto;

import com.resq.type.Cat;
import com.resq.type.Safe;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnaOut {

    private String item;

    private String title;

    private String descr;

    private Cat cat;

    private Safe safe;

    private Integer risk;

    private String expl;

    private List<String> checks;

    private List<String> quests;

    private List<String> recs;
}
