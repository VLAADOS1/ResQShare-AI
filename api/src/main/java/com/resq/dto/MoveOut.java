package com.resq.dto;

import com.resq.ai.Est;
import com.resq.dom.Dona;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoveOut {

    private Dona dona;

    private Est gain;
}
