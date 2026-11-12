package com.ptit.pttk.repository;

import com.ptit.pttk.model.Seat8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatRepository8 extends BaseRepository8 {

    public SeatRepository8() {
        super();
    }

    public Seat8 save(Seat8 seat) {
        return super.save(seat);
    }

    public Optional<Seat8> findById(Integer id) {
        return super.findById(Seat8.class, id);
    }

    public List<Seat8> findAll() {
        return super.findAll(Seat8.class);
    }
}
