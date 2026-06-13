package com.resq.dto;

import com.resq.type.Cat;
import com.resq.type.Safe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonaIn {

    private Long donor;

    private Long acc;

    private String dname;

    private Double lat;

    private Double lng;

    private String img;

    private String title;

    private String descr;

    private Cat cat;

    private String clbl;

    private String qty;

    private String store;

    private String cond;

    private String pwin;

    private String area;

    private String notes;

    private String photo;

    private Safe safe;

    private Integer risk;

    private String expl;

    private String chk;

    private String deliv;

    private Integer rad;

    private Boolean test;

    private java.util.List<String> imgs;
}
