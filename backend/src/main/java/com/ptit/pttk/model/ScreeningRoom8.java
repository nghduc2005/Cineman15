package com.ptit.pttk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "screening_room_8")
public class ScreeningRoom8 {

    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "row_number")
    private Integer rowNumber;

    @Column(name = "col_number")
    private Integer colNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RoomStatus8 status = RoomStatus8.ACTIVE;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "room_format_8",
        joinColumns = @JoinColumn(name = "screening_room_id"),
        inverseJoinColumns = @JoinColumn(name = "format_id")
    )
    private Set<Format8> formatList = new HashSet<>();

    @OneToMany(mappedBy = "screeningRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("screeningRoom")
    private List<Seat8> seatList = new ArrayList<>();

    public ScreeningRoom8() {
    }

    public ScreeningRoom8(String id, String name, Integer rowNumber, Integer colNumber, RoomStatus8 status) {
        this.id = id;
        this.name = name;
        this.rowNumber = rowNumber;
        this.colNumber = colNumber;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getColNumber() {
        return colNumber;
    }

    public void setColNumber(Integer colNumber) {
        this.colNumber = colNumber;
    }

    public RoomStatus8 getStatus() {
        return status;
    }

    public void setStatus(RoomStatus8 status) {
        this.status = status;
    }

    public List<Seat8> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<Seat8> seatList) {
        this.seatList = seatList;
    }

    public Set<Format8> getFormatList() {
        return formatList;
    }

    public void setFormatList(Set<Format8> formatList) {
        this.formatList = formatList;
    }
}
