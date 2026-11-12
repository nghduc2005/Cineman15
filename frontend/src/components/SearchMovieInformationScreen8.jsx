import React, { useState, useEffect } from 'react';
import { showtimeApi } from '../api/showtimeApi';

/**
 * Màn hình Tìm kiếm thông tin phim, phục vụ người dùng tra cứu danh sách phim theo từ khóa,
 * xem các thông tin tổng quan và điều hướng xem chi tiết từng bộ phim.
 */
export default function SearchMovieInformationScreen8({ onSelectMovie }) {
  const [keyword, setKeyword] = useState('');
  const [movieList, setMovieList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alertMessage, setAlertMessage] = useState({ type: '', text: '' });

  useEffect(() => {
    getMovieList();
  }, []);

  const getMovieList = async () => {
    try {
      setLoading(true);
      setAlertMessage({ type: '', text: '' });
      setKeyword('');
      const data = await showtimeApi.getMovieList();
      setMovieList(data || []);
      if (!data || data.length === 0) {
        setAlertMessage({
          type: 'warning',
          text: 'Hiện không có bộ phim nào trong cơ sở dữ liệu.',
        });
      }
    } catch (err) {
      console.error('Lỗi khi tải danh sách phim:', err);
      setAlertMessage({
        type: 'danger',
        text: 'Không thể kết nối đến máy chủ để tải danh sách phim.',
      });
    } finally {
      setLoading(false);
    }
  };

  /**
   * Tìm kiếm phim theo từ khóa:
   * - Nếu từ khóa rỗng (luồng ngoại lệ 2): nạp toàn bộ danh sách phim đang trong thời gian chiếu.
   * - Nếu không có kết quả phù hợp (luồng ngoại lệ 3): hiển thị cảnh báo 'Không tìm thấy phim phù hợp'.
   */
  const getMovieBySearchKeyword = async (searchKw) => {
    try {
      setLoading(true);
      setAlertMessage({ type: '', text: '' });
      const trimmed = searchKw.trim();

      const data = await showtimeApi.getMovieBySearchKeyword(trimmed);
      setMovieList(data || []);

      if (!data || data.length === 0) {
        setAlertMessage({
          type: 'warning',
          text: 'Không tìm thấy phim phù hợp',
        });
      }
    } catch (err) {
      console.error('Lỗi khi tìm kiếm phim:', err);
      setAlertMessage({
        type: 'danger',
        text: 'Đã xảy ra lỗi khi tìm kiếm phim.',
      });
    } finally {
      setLoading(false);
    }
  };

  const handleSearchSubmit = (e) => {
    if (e) e.preventDefault();
    getMovieBySearchKeyword(keyword);
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const parts = dateStr.split('-');
    if (parts.length === 3) {
      return `${parts[2]}/${parts[1]}/${parts[0]}`;
    }
    return dateStr;
  };

  // Duy trì hiển thị tối thiểu 8 hàng trong bảng để khớp đúng bố cục wireframe mẫu trong docx
  const minRows = 8;
  const emptyRowsCount = Math.max(0, minRows - movieList.length);

  return (
    <div className="screen-container">
      <h2 className="screen-title">Tìm kiếm thông tin phim</h2>

      <div className="action-bar-three">
        <div className="left-actions">
          <button 
            type="button" 
            className="btn btn-cyan"
            onClick={getMovieList}
            title="Tải lại toàn bộ danh sách phim"
          >
            Danh sách phim
          </button>
        </div>

        <form onSubmit={handleSearchSubmit} className="right-actions" style={{ display: 'flex', gap: '0' }}>
          <input
            id="inKeyword"
            type="text"
            className="wireframe-input"
            style={{ width: '260px', borderRight: 'none' }}
            placeholder="Nhập từ khóa..."
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />

          <button
            id="subSearch"
            type="submit"
            className="btn btn-green"
            style={{ whiteSpace: 'nowrap' }}
          >
            Tìm kiếm
          </button>
        </form>
      </div>

      {alertMessage.text && (
        <div className={`alert-box alert-${alertMessage.type}`}>
          <span>{alertMessage.text}</span>
          <button className="btn btn-gray" onClick={() => setAlertMessage({ type: '', text: '' })}>Đóng</button>
        </div>
      )}

      {loading && <div style={{ textAlign: 'center', margin: '12px 0', color: '#666' }}>Đang tìm kiếm...</div>}

      <div className="wireframe-table-container">
        <table id="outMovieResult" className="wireframe-table">
          <thead>
            <tr>
              <th style={{ width: '60px' }}>STT</th>
              <th style={{ width: '110px' }}>Mã phim</th>
              <th>Tên phim</th>
              <th style={{ width: '130px' }}>Ngày chiếu</th>
              <th style={{ width: '140px' }}>Ngày dừng chiếu</th>
              <th style={{ width: '120px' }}>Thời lượng</th>
              <th style={{ width: '120px' }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {movieList.map((movie, index) => (
              <tr 
                key={movie.id || index}
                style={{ cursor: 'pointer' }}
                onClick={() => onSelectMovie && onSelectMovie(movie.id)}
                title="Nhấp để xem chi tiết phim"
              >
                <td>{index + 1}</td>
                <td style={{ fontWeight: 600 }}>{movie.code || `P${String(movie.id).padStart(3, '0')}`}</td>
                <td style={{ textAlign: 'left', paddingLeft: '12px', fontWeight: 500 }}>
                  {movie.name}
                </td>
                <td>{formatDate(movie.releaseDate)}</td>
                <td>{formatDate(movie.endDate)}</td>
                <td>{movie.runtime} phút</td>
                <td>
                  <button
                    type="button"
                    className="btn btn-gray"
                    style={{ fontSize: '0.82rem', padding: '3px 10px' }}
                    onClick={(e) => {
                      e.stopPropagation();
                      if (onSelectMovie) onSelectMovie(movie.id);
                    }}
                  >
                    Chi tiết
                  </button>
                </td>
              </tr>
            ))}

            {Array.from({ length: emptyRowsCount }).map((_, idx) => (
              <tr key={`empty-${idx}`}>
                <td className="empty-cell">&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
                <td>&nbsp;</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

