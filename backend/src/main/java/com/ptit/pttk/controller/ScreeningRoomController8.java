package com.ptit.pttk.controller;

import com.ptit.pttk.model.RoomStatus8;
import com.ptit.pttk.model.ScreeningRoom8;
import com.ptit.pttk.repository.ScreeningRoomRepository8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Điều khiển lọc danh sách phòng chiếu thỏa mãn điều kiện thời gian và định dạng phim,
 * phục vụ việc chọn phòng hợp lệ trong form Lên lịch chiếu.
 */
@RestController
@RequestMapping("/api/screeningrooms")
public class ScreeningRoomController8 {

    private final ScreeningRoomRepository8 screeningRoomRepository8;

    @Autowired
    public ScreeningRoomController8(ScreeningRoomRepository8 screeningRoomRepository8) {
        this.screeningRoomRepository8 = screeningRoomRepository8;
    }

    /**
     * Lọc các phòng chiếu đang hoạt động (ACTIVE), hỗ trợ định dạng phim và không bị trùng
     * lịch chiếu trong khoảng thời gian [startTime, endTime].
     */
    @GetMapping
    public ResponseEntity<List<ScreeningRoom8>> getScreeningRoomList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate showtime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
            @RequestParam(required = false) Integer formatId) {

        List<ScreeningRoom8> rooms;
        if (showtime != null && startTime != null && endTime != null) {
            rooms = screeningRoomRepository8.getScreeningRoomList(showtime, startTime, endTime, formatId);
        } else {
            rooms = screeningRoomRepository8.findByStatus(RoomStatus8.ACTIVE);
        }
        return ResponseEntity.ok(rooms);
    }
}

