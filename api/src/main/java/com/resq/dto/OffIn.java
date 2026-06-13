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
public class OffIn {

    private Long dona;

    private Long tgt;

    private Ttype ttype;
}
