package com.ptit.pttk.repository;

import com.ptit.pttk.model.Format8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FormatRepository8 extends BaseRepository8 {

    public FormatRepository8() {
        super();
    }

    public Format8 save(Format8 format) {
        return super.save(format);
    }

    public Optional<Format8> findById(Integer id) {
        return super.findById(Format8.class, id);
    }

    public List<Format8> findAll() {
        return super.findAll(Format8.class);
    }
}

