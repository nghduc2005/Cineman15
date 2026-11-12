package com.ptit.pttk.controller;

import com.ptit.pttk.model.TimeSlot8;
import com.ptit.pttk.repository.TimeSlotRepository8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Điều khiển cung cấp danh mục khung giờ chuẩn của rạp chiếu,
 * phục vụ việc lựa chọn giờ chiếu trong form Lên lịch chiếu.
 */
@RestController
@RequestMapping("/api/timeslots")
public class TimeSlotController8 {

    private final TimeSlotRepository8 timeSlotRepository8;

    @Autowired
    public TimeSlotController8(TimeSlotRepository8 timeSlotRepository8) {
        this.timeSlotRepository8 = timeSlotRepository8;
    }

    @GetMapping
    public ResponseEntity<List<TimeSlot8>> getTimeSlotList() {
        List<TimeSlot8> timeSlots = timeSlotRepository8.getTimeSlotList();
        return ResponseEntity.ok(timeSlots);
    }
}

