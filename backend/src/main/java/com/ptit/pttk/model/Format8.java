package com.ptit.pttk.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "format_8")
public class Format8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "format_charge", precision = 12, scale = 2)
    private BigDecimal formatCharge;

    public Format8() {
    }

    public Format8(Integer id, String name, BigDecimal formatCharge) {
        this.id = id;
        this.name = name;
        this.formatCharge = formatCharge;
    }

    public Format8(String name, BigDecimal formatCharge) {
        this.name = name;
        this.formatCharge = formatCharge;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFormatCharge() {
        return formatCharge;
    }

    public void setFormatCharge(BigDecimal formatCharge) {
        this.formatCharge = formatCharge;
    }
}
