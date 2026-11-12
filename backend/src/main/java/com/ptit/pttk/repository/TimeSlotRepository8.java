package com.ptit.pttk.repository;

import com.ptit.pttk.model.TimeSlot8;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repository truy vấn danh mục khung giờ chiếu chuẩn của rạp.
 */
@Repository
public class TimeSlotRepository8 extends BaseRepository8 {

    public TimeSlotRepository8() {
        super();
    }

    public List<TimeSlot8> getTimeSlotList() {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery("SELECT t FROM TimeSlot8 t ORDER BY t.startTime ASC", TimeSlot8.class).getResultList();
        }
        return Collections.emptyList();
    }

    public TimeSlot8 save(TimeSlot8 timeSlot) {
        return super.save(timeSlot);
    }

    public Optional<TimeSlot8> findById(Integer id) {
        return super.findById(TimeSlot8.class, id);
    }

    public List<TimeSlot8> findAll() {
        return getTimeSlotList();
    }
}

