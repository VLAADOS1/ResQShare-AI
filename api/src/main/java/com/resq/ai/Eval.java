package com.resq.ai;

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
public class Eval {

    private Safe safe;

    private Integer risk;

    private List<String> checks;

    private List<String> quests;

    private String expl;
}
