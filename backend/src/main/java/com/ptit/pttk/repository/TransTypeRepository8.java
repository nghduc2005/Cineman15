package com.ptit.pttk.repository;

import com.ptit.pttk.model.TransType8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TransTypeRepository8 extends BaseRepository8 {

    public TransTypeRepository8() {
        super();
    }

    public TransType8 save(TransType8 transType) {
        return super.save(transType);
    }

    public Optional<TransType8> findById(Integer id) {
        return super.findById(TransType8.class, id);
    }

    public List<TransType8> findAll() {
        return super.findAll(TransType8.class);
    }
}

