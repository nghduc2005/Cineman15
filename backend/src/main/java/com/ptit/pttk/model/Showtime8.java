package com.ptit.pttk.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "showtime_8")
public class Showtime8 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate showtime; // Date of screening

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot8 timeSlot;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "screening_room_id", nullable = false)
    private ScreeningRoom8 screeningRoom;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trans_type_id", nullable = false)
    private TransType8 transType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "format_id", nullable = false)
    private Format8 format;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie8 movie;

    public Showtime8() {
    }

    public Showtime8(Integer id, LocalDate showtime, LocalTime endTime, TimeSlot8 timeSlot,
                     ScreeningRoom8 screeningRoom, TransType8 transType, Format8 format, Movie8 movie) {
        this.id = id;
        this.showtime = showtime;
        this.endTime = endTime;
        this.timeSlot = timeSlot;
        this.screeningRoom = screeningRoom;
        this.transType = transType;
        this.format = format;
        this.movie = movie;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getShowtime() {
        return showtime;
    }

    public void setShowtime(LocalDate showtime) {
        this.showtime = showtime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public TimeSlot8 getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot8 timeSlot) {
        this.timeSlot = timeSlot;
    }

    public ScreeningRoom8 getScreeningRoom() {
        return screeningRoom;
    }

    public void setScreeningRoom(ScreeningRoom8 screeningRoom) {
        this.screeningRoom = screeningRoom;
    }

    public TransType8 getTransType() {
        return transType;
    }

    public void setTransType(TransType8 transType) {
        this.transType = transType;
    }

    public Format8 getFormat() {
        return format;
    }

    public void setFormat(Format8 format) {
        this.format = format;
    }

    public Movie8 getMovie() {
        return movie;
    }

    public void setMovie(Movie8 movie) {
        this.movie = movie;
    }
}
