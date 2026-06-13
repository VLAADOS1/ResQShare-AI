package com.resq.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resq.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Acct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @JsonIgnore
    private String pass;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    private String area;

    private Long prof;

    private Boolean admin;

    private Boolean banned;

    private Boolean test;
}
