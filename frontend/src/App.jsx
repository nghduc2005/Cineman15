import React, { useState } from 'react';
import ShowtimeManageScreen8 from './components/ShowtimeManageScreen8';
import ScheduleShowtimeScreen8 from './components/ScheduleShowtimeScreen8';
import SearchMovieInformationScreen8 from './components/SearchMovieInformationScreen8';
import MovieInformationScreen8 from './components/MovieInformationScreen8';

/**
 * Điều phối luồng giao diện giữa 2 module chính theo đặc tả BTL:
 * 1. Quản lý lịch chiếu (danh sách lịch chiếu -> form lên lịch chiếu mới).
 * 2. Tìm kiếm thông tin phim (bảng tìm kiếm -> màn hình xem chi tiết phim).
 */
export default function App() {
  const [activeModule, setActiveModule] = useState('showtimes');
  const [showtimeSubScreen, setShowtimeSubScreen] = useState('list');
  const [refreshShowtimeKey, setRefreshShowtimeKey] = useState(0);

  const [movieSubScreen, setMovieSubScreen] = useState('search');
  const [selectedMovieId, setSelectedMovieId] = useState(null);

  const handleSelectModule = (mod) => {
    setActiveModule(mod);
    if (mod === 'showtimes') {
      setShowtimeSubScreen('list');
    } else {
      setMovieSubScreen('search');
    }
  };

  const handleShowtimeCreated = () => {
    setShowtimeSubScreen('list');
    setRefreshShowtimeKey((prev) => prev + 1);
  };

  const handleSelectMovieForDetail = (id) => {
    setSelectedMovieId(id);
    setMovieSubScreen('detail');
  };

  return (
    <div className="app-container">
      <header className="main-nav">
        <div className="main-nav-brand">
          HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM (CineMan)
        </div>

        <nav className="nav-tabs">
          <button
            type="button"
            className={`nav-tab-btn ${activeModule === 'showtimes' ? 'active' : ''}`}
            onClick={() => handleSelectModule('showtimes')}
          >
            Quản lý lịch chiếu
          </button>

          <button
            type="button"
            className={`nav-tab-btn ${activeModule === 'movies' ? 'active' : ''}`}
            onClick={() => handleSelectModule('movies')}
          >
            Tìm kiếm thông tin phim
          </button>
        </nav>
      </header>

      <main>
        {activeModule === 'showtimes' ? (
          showtimeSubScreen === 'list' ? (
            <ShowtimeManageScreen8
              key={refreshShowtimeKey}
              onNavigateToSchedule={() => setShowtimeSubScreen('create')}
            />
          ) : (
            <ScheduleShowtimeScreen8
              onNavigateBack={() => setShowtimeSubScreen('list')}
              onShowtimeCreated={handleShowtimeCreated}
            />
          )
        ) : (
          movieSubScreen === 'search' ? (
            <SearchMovieInformationScreen8
              onSelectMovie={handleSelectMovieForDetail}
            />
          ) : (
            <MovieInformationScreen8
              movieId={selectedMovieId}
              onNavigateBack={() => setMovieSubScreen('search')}
            />
          )
        )}
      </main>
    </div>
  );
}
