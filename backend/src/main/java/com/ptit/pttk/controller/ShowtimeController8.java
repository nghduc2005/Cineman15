package com.ptit.pttk.controller;

import com.ptit.pttk.model.Showtime8;
import com.ptit.pttk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Điều khiển trung gian xử lý nghiệp vụ quản lý và tạo mới lịch chiếu,
 * phục vụ module Lên lịch chiếu của hệ thống.
 */
@RestController
@RequestMapping("/api/showtimes")
public class ShowtimeController8 {

    private final ShowtimeRepository8 showtimeRepository8;
    private final TimeSlotRepository8 timeSlotRepository8;
    private final ScreeningRoomRepository8 screeningRoomRepository8;
    private final MovieRepository8 movieRepository8;
    private final FormatRepository8 formatRepository8;
    private final TransTypeRepository8 transTypeRepository8;

    @Autowired
    public ShowtimeController8(ShowtimeRepository8 showtimeRepository8,
                               TimeSlotRepository8 timeSlotRepository8,
                               ScreeningRoomRepository8 screeningRoomRepository8,
                               MovieRepository8 movieRepository8,
                               FormatRepository8 formatRepository8,
                               TransTypeRepository8 transTypeRepository8) {
        this.showtimeRepository8 = showtimeRepository8;
        this.timeSlotRepository8 = timeSlotRepository8;
        this.screeningRoomRepository8 = screeningRoomRepository8;
        this.movieRepository8 = movieRepository8;
        this.formatRepository8 = formatRepository8;
        this.transTypeRepository8 = transTypeRepository8;
    }

    /**
     * Tạo lịch chiếu mới với kiểm tra xung đột thời gian trong phòng chiếu
     * nhằm ngăn ngừa trùng lịch chiếu tại cùng một phòng.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createScheduleShowTime(@RequestBody Showtime8 newShowtime) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (newShowtime.getTimeSlot() != null && newShowtime.getTimeSlot().getId() != null) {
                timeSlotRepository8.findById(newShowtime.getTimeSlot().getId()).ifPresent(newShowtime::setTimeSlot);
            }
            if (newShowtime.getScreeningRoom() != null && newShowtime.getScreeningRoom().getId() != null) {
                screeningRoomRepository8.findById(newShowtime.getScreeningRoom().getId()).ifPresent(newShowtime::setScreeningRoom);
            }
            if (newShowtime.getMovie() != null && newShowtime.getMovie().getId() != null) {
                movieRepository8.findById(newShowtime.getMovie().getId()).ifPresent(newShowtime::setMovie);
            }
            if (newShowtime.getFormat() != null && newShowtime.getFormat().getId() != null) {
                formatRepository8.findById(newShowtime.getFormat().getId()).ifPresent(newShowtime::setFormat);
            }
            if (newShowtime.getTransType() != null && newShowtime.getTransType().getId() != null) {
                transTypeRepository8.findById(newShowtime.getTransType().getId()).ifPresent(newShowtime::setTransType);
            }

            // Kiểm tra xung đột thời gian chiếu: đảm bảo phòng không có 2 suất giao thoa thời gian
            if (newShowtime.getScreeningRoom() != null &&
                newShowtime.getShowtime() != null &&
                newShowtime.getTimeSlot() != null &&
                newShowtime.getEndTime() != null) {

                List<Showtime8> conflicts = showtimeRepository8.findConflictingShowtimes(
                    newShowtime.getScreeningRoom().getId(),
                    newShowtime.getShowtime(),
                    newShowtime.getTimeSlot().getStartTime(),
                    newShowtime.getEndTime()
                );

                if (!conflicts.isEmpty()) {
                    response.put("success", false);
                    response.put("message", "Phòng chiếu đã có lịch chiếu trong khung giờ này!");
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
                }
            }

            Showtime8 saved = showtimeRepository8.createScheduleShowtime(newShowtime);
            boolean success = saved.getId() != null;
            response.put("success", success);
            response.put("data", saved);
            response.put("message", "Lên lịch chiếu thành công!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi tạo lịch chiếu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<List<Showtime8>> getScheduleShowtimeList() {
        List<Showtime8> showtimes = showtimeRepository8.getScheduleShowtimeList();
        return ResponseEntity.ok(showtimes);
    }
}

