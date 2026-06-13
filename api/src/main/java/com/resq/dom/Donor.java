package com.resq.dom;

import com.resq.type.Cat;
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
@Table(name = "donors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Donor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long acc;

    private String type;

    private String name;

    private String area;

    private Double lat;

    private Double lng;

    @Enumerated(EnumType.STRING)
    private Cat dtype;

    private String pref;

    @jakarta.persistence.Column(length = 400)
    private String bio;

    private Boolean test;
}
