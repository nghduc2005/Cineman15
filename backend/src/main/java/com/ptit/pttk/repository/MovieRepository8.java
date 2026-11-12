package com.ptit.pttk.repository;

import com.ptit.pttk.model.Movie8;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Repository thao tác với bảng dữ liệu phim, kế thừa BaseRepository8 để sẵn sàng
 * kết nối JDBC và EntityManager JPA.
 */
@Repository
public class MovieRepository8 extends BaseRepository8 {

    public MovieRepository8() {
        super();
    }

    /**
     * Lấy danh sách phim kèm nhãn, thể loại, định dạng, bản dịch (JOIN FETCH)
     * để tránh lỗi LazyInitializationException khi hiển thị danh sách trên frontend.
     */
    public List<Movie8> getMovieList() {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT DISTINCT m FROM Movie8 m " +
                "LEFT JOIN FETCH m.movieLabel " +
                "LEFT JOIN FETCH m.categoryList " +
                "LEFT JOIN FETCH m.formatList " +
                "LEFT JOIN FETCH m.transTypeList " +
                "ORDER BY m.id ASC", Movie8.class).getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Tìm kiếm phim theo tên hoặc mã. Nếu keyword rỗng, trả về tất cả phim
     * nhằm đáp ứng luồng ngoại lệ 2 trong kịch bản BTL.
     */
    public List<Movie8> getMovieBySearchKeyword(String keyword) {
        getConnection();
        if (entityManager != null) {
            return entityManager.createQuery(
                "SELECT DISTINCT m FROM Movie8 m " +
                "LEFT JOIN FETCH m.movieLabel " +
                "LEFT JOIN FETCH m.categoryList " +
                "LEFT JOIN FETCH m.formatList " +
                "LEFT JOIN FETCH m.transTypeList " +
                "WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(m.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
                "       OR LOWER(m.code) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
                "ORDER BY m.name ASC", Movie8.class)
                .setParameter("keyword", keyword)
                .getResultList();
        }
        return Collections.emptyList();
    }

    /**
     * Tải toàn bộ chi tiết phim gồm đoàn làm phim, nhãn, thể loại, định dạng, bản dịch
     * phục vụ màn hình xem chi tiết thông tin phim.
     */
    public Optional<Movie8> getMovieInformation(Integer id) {
        getConnection();
        if (entityManager != null) {
            List<Movie8> results = entityManager.createQuery(
                "SELECT DISTINCT m FROM Movie8 m " +
                "LEFT JOIN FETCH m.movieLabel " +
                "LEFT JOIN FETCH m.categoryList " +
                "LEFT JOIN FETCH m.formatList " +
                "LEFT JOIN FETCH m.transTypeList " +
                "LEFT JOIN FETCH m.crewMemberList " +
                "WHERE m.id = :id", Movie8.class)
                .setParameter("id", id)
                .getResultList();
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        }
        return Optional.empty();
    }

    public Movie8 save(Movie8 movie) {
        return super.save(movie);
    }

    public Optional<Movie8> findById(Integer id) {
        return super.findById(Movie8.class, id);
    }

    public long count() {
        return super.count(Movie8.class);
    }
}

