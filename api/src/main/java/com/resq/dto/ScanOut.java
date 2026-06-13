package com.resq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScanOut {

    private String item;

    private String clbl;

    private String cat;

    private String qty;

    private String cond;

    private String note;
}
