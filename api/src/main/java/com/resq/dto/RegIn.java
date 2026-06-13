package com.resq.dto;

import com.resq.type.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegIn {

    private String email;

    private String pass;

    private Role role;

    private String name;

    private String area;
}
