package com.resq.dom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "msgs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Msg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dona;

    private Long frm;

    @Column(name = "rcv")
    private Long to;

    private String nameFrm;

    private String nameTo;

    @Column(length = 1000)
    private String body;

    private Long ts;
}
