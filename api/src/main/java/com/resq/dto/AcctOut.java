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
public class AcctOut {

    private Long id;

    private String email;

    private Role role;

    private String name;

    private String area;

    private Long prof;

    private Boolean admin;

    private Boolean banned;

    private Boolean test;
}
