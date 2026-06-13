package com.resq.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardIn {

    private String title;

    private String emoji;

    private String descr;

    private String metric;

    private Integer goal;

    private String gift;
}
