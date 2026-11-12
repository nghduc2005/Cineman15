package com.ptit.pttk.repository;

import com.ptit.pttk.model.MovieLabel8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MovieLabelRepository8 extends BaseRepository8 {

    public MovieLabelRepository8() {
        super();
    }

    public MovieLabel8 save(MovieLabel8 label) {
        return super.save(label);
    }

    public Optional<MovieLabel8> findById(Integer id) {
        return super.findById(MovieLabel8.class, id);
    }

    public List<MovieLabel8> findAll() {
        return super.findAll(MovieLabel8.class);
    }
}
