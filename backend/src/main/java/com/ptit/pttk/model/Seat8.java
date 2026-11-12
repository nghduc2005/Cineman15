package com.ptit.pttk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "seat_8")
public class Seat8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "seat_row", nullable = false, length = 10)
    private String row;

    @Column(name = "seat_number", nullable = false)
    private Integer number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SeatStatus8 status = SeatStatus8.ACTIVE;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_tier_id")
    private SeatTier8 seatTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screening_room_id")
    @JsonIgnoreProperties("seatList")
    private ScreeningRoom8 screeningRoom;

    public Seat8() {
    }

    public Seat8(Integer id, String row, Integer number, SeatStatus8 status, SeatTier8 seatTier, ScreeningRoom8 screeningRoom) {
        this.id = id;
        this.row = row;
        this.number = number;
        this.status = status;
        this.seatTier = seatTier;
        this.screeningRoom = screeningRoom;
    }

    public Seat8(String row, Integer number, SeatStatus8 status, SeatTier8 seatTier, ScreeningRoom8 screeningRoom) {
        this.row = row;
        this.number = number;
        this.status = status;
        this.seatTier = seatTier;
        this.screeningRoom = screeningRoom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public SeatStatus8 getStatus() {
        return status;
    }

    public void setStatus(SeatStatus8 status) {
        this.status = status;
    }

    public SeatTier8 getSeatTier() {
        return seatTier;
    }

    public void setSeatTier(SeatTier8 seatTier) {
        this.seatTier = seatTier;
    }

    public ScreeningRoom8 getScreeningRoom() {
        return screeningRoom;
    }

    public void setScreeningRoom(ScreeningRoom8 screeningRoom) {
        this.screeningRoom = screeningRoom;
    }
}
