package com.resq.dom;

import com.resq.type.Stat;
import com.resq.type.Ttype;
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
@Table(name = "matchs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dona;

    private Long tgt;

    @Enumerated(EnumType.STRING)
    private Ttype ttype;

    private Integer score;

    @Column(length = 400)
    private String reason;

    @Enumerated(EnumType.STRING)
    private Stat stat;
}
