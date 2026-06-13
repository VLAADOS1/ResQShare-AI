package com.resq.dto;

import com.resq.type.Cat;
import com.resq.type.Urg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqIn {

    private Long recip;

    private Long acc;

    private String name;

    private Double lat;

    private Double lng;

    private String area;

    private Boolean test;

    private Cat cat;

    private String descr;

    private String qty;

    private Urg urg;

    private Integer radius;

    private String avail;

    private String restr;

    private String pub;

    private String lang;
}
