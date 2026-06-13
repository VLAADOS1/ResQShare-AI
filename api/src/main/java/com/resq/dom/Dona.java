package com.resq.dom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resq.type.Cat;
import com.resq.type.Safe;
import com.resq.type.Stat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "donas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long donor;

    private Long acc;

    private String dname;

    private Double lat;

    private Double lng;

    private Boolean pic;

    @Lob
    @JsonIgnore
    private byte[] img;

    private String title;

    @Column(length = 600)
    private String descr;

    @Enumerated(EnumType.STRING)
    private Cat cat;

    private String clbl;

    private String qty;

    private String store;

    private String cond;

    private String pwin;

    private String area;

    @Column(length = 400)
    private String notes;

    private String photo;

    @Enumerated(EnumType.STRING)
    private Stat stat;

    private Integer risk;

    @Enumerated(EnumType.STRING)
    private Safe safe;

    @Column(length = 600)
    private String expl;

    @Column(length = 1000)
    private String chk;

    private String deliv;

    private Integer rad;

    private Boolean test;
}
