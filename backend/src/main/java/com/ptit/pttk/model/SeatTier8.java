package com.ptit.pttk.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seat_tier_8")
public class SeatTier8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(name = "seat_charge", precision = 12, scale = 2)
    private BigDecimal seatCharge;

    public SeatTier8() {
    }

    public SeatTier8(Integer id, String name, BigDecimal seatCharge) {
        this.id = id;
        this.name = name;
        this.seatCharge = seatCharge;
    }

    public SeatTier8(String name, BigDecimal seatCharge) {
        this.name = name;
        this.seatCharge = seatCharge;
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

    public BigDecimal getSeatCharge() {
        return seatCharge;
    }

    public void setSeatCharge(BigDecimal seatCharge) {
        this.seatCharge = seatCharge;
    }
}
