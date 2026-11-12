package com.ptit.pttk.controller;

import com.ptit.pttk.model.Movie8;
import com.ptit.pttk.repository.MovieRepository8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Điều khiển trung gian xử lý các yêu cầu liên quan đến thông tin phim,
 * phục vụ module Tìm kiếm thông tin phim và hiển thị chi tiết phim.
 */
@RestController
@RequestMapping("/api/movies")
public class MovieController8 {

    private final MovieRepository8 movieRepository8;

    @Autowired
    public MovieController8(MovieRepository8 movieRepository8) {
        this.movieRepository8 = movieRepository8;
    }

    @GetMapping
    public ResponseEntity<List<Movie8>> getMovieList() {
        List<Movie8> movies = movieRepository8.getMovieList();
        return ResponseEntity.ok(movies);
    }

    /**
     * Tìm kiếm phim theo tên hoặc mã. Nếu keyword rỗng, trả về toàn bộ phim
     * đang chiếu nhằm đáp ứng luồng ngoại lệ 2 trong đặc tả PTTK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Movie8>> getMovieBySearchKeyword(@RequestParam(required = false, defaultValue = "") String keyword) {
        List<Movie8> movies = movieRepository8.getMovieBySearchKeyword(keyword.trim());
        return ResponseEntity.ok(movies);
    }

    /**
     * Lấy đầy đủ thông tin chi tiết phim (đoàn phim, nhãn, thể loại, định dạng, bản dịch)
     * phục vụ màn hình xem chi tiết phim.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieInformation(@PathVariable Integer id) {
        return movieRepository8.getMovieInformation(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

