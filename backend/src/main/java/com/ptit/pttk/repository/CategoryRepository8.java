package com.ptit.pttk.repository;

import com.ptit.pttk.model.Category8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository8 extends BaseRepository8 {

    public CategoryRepository8() {
        super();
    }

    public Category8 save(Category8 category) {
        return super.save(category);
    }

    public Optional<Category8> findById(Integer id) {
        return super.findById(Category8.class, id);
    }

    public List<Category8> findAll() {
        return super.findAll(Category8.class);
    }
}

