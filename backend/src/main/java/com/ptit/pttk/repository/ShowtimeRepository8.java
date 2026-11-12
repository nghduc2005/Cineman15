package com.ptit.pttk.repository;

import com.ptit.pttk.model.Showtime8;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repository quản lý lưu trữ và truy vấn lịch chiếu phim,
 * kế thừa BaseRepository8 để dùng chung kết nối JDBC và JPA.
 */
@Repository
public class ShowtimeRepository8 extends BaseRepository8 {

    public ShowtimeRepository8() {
        super();
    }

    /**
     * Lưu thực thể lịch chiếu mới vào CSDL phục vụ chức năng xác nhận lên lịch chiếu.
     */
    @Transactional
    public Showtime8 createScheduleShowtime(Showtime8 newShowtime) {
        getConnection();
        if (entityManager != null) {
            if (newShowtime.getId() == null) {
                entityManager.persist(newShowtime);
                return newShowtime;
            } else {
                return entityManager.merge(newShowtime);
            }
        }
        return newShowtime;
    }

    @Transactional
    public boolean createScheduleShowTime(Showtime8 newShowtime) {
        return createScheduleShowtime(newShowtime) != null;
    }

    /**
     * Lấy danh sách lịch chiếu kèm đầy đủ thông tin phim, phòng chiếu, khung giờ, định dạng, bản dịch
     * phục vụ hiển thị trên bảng Quản lý danh sách lịch chiếu.
     */
    public List<Showtime8> getScheduleShowtimeList() {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT s FROM Showtime8 s " +
                "JOIN FETCH s.movie m " +
                "JOIN FETCH s.timeSlot ts " +
                "JOIN FETCH s.screeningRoom sr " +
                "JOIN FETCH s.transType tt " +
                "JOIN FETCH s.format f " +
                "ORDER BY s.showtime DESC, ts.startTime ASC", Showtime8.class).getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Kiểm tra xung đột thời gian chiếu của một phòng:
     * Hai khoảng [s.startTime, s.endTime] và [startTime, endTime] giao thoa khi và chỉ khi
     * s.startTime < endTime AND startTime < s.endTime.
     */
    public List<Showtime8> findConflictingShowtimes(String roomId, LocalDate showtime, LocalTime startTime, LocalTime endTime) {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT s FROM Showtime8 s " +
                "WHERE s.screeningRoom.id = :roomId " +
                "AND s.showtime = :showtime " +
                "AND s.timeSlot.startTime < :endTime " +
                "AND :startTime < s.endTime", Showtime8.class)
                .setParameter("roomId", roomId)
                .setParameter("showtime", showtime)
                .setParameter("startTime", startTime)
                .setParameter("endTime", endTime)
                .getResultList();
        }
        return Collections.emptyList();
    }

    public Showtime8 save(Showtime8 showtime) {
        return createScheduleShowtime(showtime);
    }

    public Optional<Showtime8> findById(Integer id) {
        return super.findById(Showtime8.class, id);
    }
}

