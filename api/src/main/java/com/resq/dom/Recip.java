package com.resq.dom;

import com.resq.type.Urg;
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
@Table(name = "recips")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long acc;

    private String atype;

    private String area;

    private Double lat;

    private Double lng;

    private Integer radius;

    private String needs;

    private String diet;

    private String avail;

    private Boolean deliv;

    @Enumerated(EnumType.STRING)
    private Urg urg;

    private Boolean test;
}
