package com.resq.dto;

import com.resq.type.Ttype;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MchOut {

    private Long tgt;

    private Long acc;

    private Ttype ttype;

    private String name;

    private Double dist;

    private Integer score;

    private String reason;

    private String kind;

    private String area;

    private String need;

    private boolean accepts;

    private boolean urgent;

    private boolean test;
}
