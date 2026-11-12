package com.ptit.pttk.repository;

import com.ptit.pttk.model.SeatTier8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class SeatTierRepository8 extends BaseRepository8 {

    public SeatTierRepository8() {
        super();
    }

    public SeatTier8 save(SeatTier8 seatTier) {
        return super.save(seatTier);
    }

    public Optional<SeatTier8> findById(Integer id) {
        return super.findById(SeatTier8.class, id);
    }

    public List<SeatTier8> findAll() {
        return super.findAll(SeatTier8.class);
    }
}

