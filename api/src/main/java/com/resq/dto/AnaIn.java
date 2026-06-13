package com.resq.dto;

import com.resq.type.Cat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnaIn {

    private Cat cat;

    private String clbl;

    private String qty;

    private String store;

    private String cond;

    private String pwin;

    private String area;

    private String notes;

    private String photo;

    private String img;

    private String lang;

    private Double lat;

    private Double lng;
}
