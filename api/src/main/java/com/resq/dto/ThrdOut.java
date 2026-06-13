package com.resq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThrdOut {

    private Long other;

    private String oname;

    private Long dona;

    private String last;

    private Long lastFrm;

    private Long ts;
}
