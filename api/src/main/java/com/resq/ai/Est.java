package com.resq.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Est {

    private Double kg;

    private Integer meals;

    private Double co2;

    private Integer people;
}
