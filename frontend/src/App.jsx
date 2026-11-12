import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import ShowtimeManageScreen8 from './components/ShowtimeManageScreen8';
import ScheduleShowtimeScreen8 from './components/ScheduleShowtimeScreen8';
import SearchMovieInformationScreen8 from './components/SearchMovieInformationScreen8';
import MovieInformationScreen8 from './components/MovieInformationScreen8';

/**
 * Định tuyến 2 giao diện độc lập qua react-router-dom:
 * - Module Lên lịch chiếu: đường dẫn /showtime (danh sách) và /showtime/schedule (thêm mới).
 * - Module Tìm kiếm thông tin phim: đường dẫn /search (tìm kiếm) và /search/:id (chi tiết phim).
 * Giao diện hoàn toàn không có khung viền ngoài và không có nút chuyển tab theo yêu cầu tối giản.
 */
export default function App() {
  return (
    <BrowserRouter>
      <div className="app-container">
        <main>
          <Routes>
            {/* Chuyển hướng mặc định sang /showtime */}
            <Route path="/" element={<Navigate to="/showtime" replace />} />

            {/* Giao diện 1: Module Lịch chiếu */}
            <Route path="/showtime" element={<ShowtimeManageScreen8 />} />
            <Route path="/showtime/schedule" element={<ScheduleShowtimeScreen8 />} />
            <Route path="/showtimes" element={<Navigate to="/showtime" replace />} />

            {/* Giao diện 2: Module Tìm kiếm thông tin phim */}
            <Route path="/search" element={<SearchMovieInformationScreen8 />} />
            <Route path="/search/:id" element={<MovieInformationScreen8 />} />

            {/* Đường dẫn không xác định chuyển về /showtime */}
            <Route path="*" element={<Navigate to="/showtime" replace />} />
          </Routes>
        </main>
      </div>
    </BrowserRouter>
  );
}
