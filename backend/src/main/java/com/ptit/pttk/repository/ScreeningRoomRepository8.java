package com.ptit.pttk.repository;

import com.ptit.pttk.model.RoomStatus8;
import com.ptit.pttk.model.ScreeningRoom8;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repository thao tác phòng chiếu, kế thừa BaseRepository8 để dùng chung kết nối CSDL.
 */
@Repository
public class ScreeningRoomRepository8 extends BaseRepository8 {

    public ScreeningRoomRepository8() {
        super();
    }

    /**
     * Lọc phòng chiếu thỏa mãn:
     * 1. Đang hoạt động (ACTIVE).
     * 2. Hỗ trợ định dạng kỹ thuật (2D, 3D, IMAX, ...) của phim.
     * 3. Chưa bị trùng lịch chiếu trong khoảng thời gian [startTime, endTime] (dùng mệnh đề NOT IN).
     */
    public List<ScreeningRoom8> getScreeningRoomList(LocalDate showtime, LocalTime startTime, LocalTime endTime, Integer formatId) {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT DISTINCT r FROM ScreeningRoom8 r " +
                "LEFT JOIN r.formatList f " +
                "WHERE r.status = :activeStatus " +
                "AND (:formatId IS NULL OR f.id = :formatId) " +
                "AND r.id NOT IN (" +
                "    SELECT s.screeningRoom.id FROM Showtime8 s " +
                "    WHERE s.showtime = :showtime " +
                "    AND s.timeSlot.startTime < :endTime " +
                "    AND :startTime < s.endTime" +
                ") ORDER BY r.name ASC", ScreeningRoom8.class)
                .setParameter("activeStatus", RoomStatus8.ACTIVE)
                .setParameter("showtime", showtime)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .setParameter("formatId", formatId)
                .getResultList();
        }
        return Collections.emptyList();
    }

    public List<ScreeningRoom8> findByStatus(RoomStatus8 status) {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT r FROM ScreeningRoom8 r WHERE r.status = :status ORDER BY r.name ASC", ScreeningRoom8.class)
                .setParameter("status", status)
                .getResultList();
        }
        return Collections.emptyList();
    }

    public ScreeningRoom8 save(ScreeningRoom8 room) {
        return super.save(room);
    }

    public Optional<ScreeningRoom8> findById(String id) {
        return super.findById(ScreeningRoom8.class, id);
    }

    public List<ScreeningRoom8> findAll() {
        return super.findAll(ScreeningRoom8.class);
    }
}

