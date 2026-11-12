package com.ptit.pttk.repository;

import com.ptit.pttk.model.CrewMember8;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CrewMemberRepository8 extends BaseRepository8 {

    public CrewMemberRepository8() {
        super();
    }

    public CrewMember8 save(CrewMember8 crewMember) {
        return super.save(crewMember);
    }

    public Optional<CrewMember8> findById(Integer id) {
        return super.findById(CrewMember8.class, id);
    }

    public List<CrewMember8> findAll() {
        return super.findAll(CrewMember8.class);
    }
}

