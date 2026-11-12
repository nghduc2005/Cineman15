import React, { useState, useEffect } from 'react';
import { showtimeApi } from '../api/showtimeApi';

/**
 * Màn hình Quản lý danh sách lịch chiếu, phục vụ hiển thị các suất chiếu hiện có
 * và cung cấp nút điều hướng sang màn hình Lên lịch chiếu mới.
 */
export default function ShowtimeManageScreen8({ onNavigateToSchedule }) {
  const [showtimeList, setShowtimeList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const getShowtimeList = async () => {
    try {
      setLoading(true);
      setErrorMessage('');
      const data = await showtimeApi.getScheduleShowtimeList();
      setShowtimeList(data);
    } catch (err) {
      console.error('Lỗi khi tải danh sách lịch chiếu:', err);
      setErrorMessage('Không thể kết nối đến máy chủ hoặc tải danh sách lịch chiếu.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    getShowtimeList();
  }, []);

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const parts = dateStr.split('-');
    if (parts.length === 3) {
      return `${parts[2]}/${parts[1]}/${parts[0]}`;
    }
    return dateStr;
  };

  const formatTime = (timeStr) => {
    if (!timeStr) return '';
    return timeStr.substring(0, 5);
  };

  // Duy trì hiển thị tối thiểu 8 hàng (bổ sung hàng trống) nhằm đảm bảo bố cục trực quan đúng mẫu wireframe
  const minRows = 8;
  const emptyRowsCount = Math.max(0, minRows - showtimeList.length);

  return (
    <div className="screen-container">
      <h2 className="screen-title">
        Quản lý lịch chiếu
      </h2>

      <div className="action-bar-three">
        <div className="left-actions">
          <button 
            type="button" 
            className="btn btn-cyan"
            onClick={getShowtimeList}
            title="Tải lại danh sách lịch chiếu"
          >
            Danh sách lịch chiếu
          </button>

          <button 
            type="button" 
            className="btn btn-yellow"
            title="Quản lý khung giờ"
            onClick={() => {}}
          >
            Quản lý khung giờ
          </button>
        </div>

        <div className="right-actions">
          <a
            id="subScheduleShowtime"
            href="#schedule"
            className="btn btn-green"
            onClick={(e) => {
              e.preventDefault();
              if (onNavigateToSchedule) onNavigateToSchedule();
            }}
          >
            Thêm lịch chiếu
          </a>
        </div>
      </div>

      {errorMessage && (
        <div className="alert-box alert-danger">
          <span>{errorMessage}</span>
          <button className="btn btn-gray" onClick={() => setErrorMessage('')}>Đóng</button>
        </div>
      )}

      {loading && <div style={{ textAlign: 'center', margin: '12px 0', color: '#666' }}>Đang tải dữ liệu...</div>}

      <div className="wireframe-table-container">
        <table className="wireframe-table">
          <thead>
            <tr>
              <th style={{ width: '60px' }}>STT</th>
              <th style={{ width: '130px' }}>Ngày chiếu</th>
              <th style={{ width: '110px' }}>Giờ bắt đầu</th>
              <th style={{ width: '110px' }}>Giờ kết thúc</th>
              <th style={{ width: '160px' }}>Phòng chiếu</th>
              <th>Phim</th>
              <th style={{ width: '100px' }}>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {showtimeList.map((item, index) => (
              <tr key={item.id || index}>
                <td>{index + 1}</td>
                <td>{formatDate(item.showtime)}</td>
                <td>{formatTime(item.timeSlot?.startTime)}</td>
                <td>{formatTime(item.endTime)}</td>
                <td>{item.screeningRoom?.name || 'N/A'}</td>
                <td style={{ textAlign: 'left', paddingLeft: '12px' }}>
                  {item.movie?.name || 'N/A'}
                  {item.format?.name && (
                    <span style={{ fontSize: '0.8rem', color: '#555', marginLeft: '8px' }}>
                      ({item.format.name} - {item.transType?.name || 'Sub'})
                    </span>
                  )}
                </td>
                <td>
                  <span style={{ color: '#0284c7', fontSize: '0.85rem' }}>Các thao tác</span>
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
