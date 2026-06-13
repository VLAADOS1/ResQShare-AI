package com.resq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgIn {

    private Long dona;

    private Long frm;

    private Long to;

    private String nameFrm;

    private String nameTo;

    private String body;
}
