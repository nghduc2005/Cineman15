package com.ptit.pttk.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "time_slot_8")
public class TimeSlot8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "base_price", precision = 12, scale = 2)
    private BigDecimal basePrice;

    public TimeSlot8() {
    }

    public TimeSlot8(Integer id, String name, LocalTime startTime, BigDecimal basePrice) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.basePrice = basePrice;
    }

    public TimeSlot8(String name, LocalTime startTime, BigDecimal basePrice) {
        this.name = name;
        this.startTime = startTime;
        this.basePrice = basePrice;
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

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}
