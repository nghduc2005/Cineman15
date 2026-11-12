import React, { useState, useEffect } from 'react';
import { showtimeApi } from '../api/showtimeApi';

/**
 * Màn hình Xem chi tiết thông tin phim, phục vụ hiển thị toàn bộ thuộc tính của một bộ phim
 * bao gồm poster, trailer video (qua iframe), thông tin phân loại, bản dịch và đoàn làm phim.
 */
export default function MovieInformationScreen8({ movieId, onNavigateBack }) {
  const [movieInformation, setMovieInformation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    if (movieId) {
      getMovieInformation(movieId);
    }
  }, [movieId]);

  const getMovieInformation = async (id) => {
    try {
      setLoading(true);
      setErrorMessage('');
      const data = await showtimeApi.getMovieInformation(id);
      setMovieInformation(data);
    } catch (err) {
      console.error('Lỗi khi tải chi tiết phim:', err);
      setErrorMessage('Không thể tìm thấy thông tin chi tiết của bộ phim này.');
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateStr) => {
    if (!dateStr) return '';
    const parts = dateStr.split('-');
    if (parts.length === 3) {
      return `${parts[2]}/${parts[1]}/${parts[0]}`;
    }
    return dateStr;
  };

  /**
   * Chuyển đổi URL YouTube thông thường (watch?v=) sang chuẩn nhúng (embed/) nhằm tránh bị
   * YouTube chặn bởi cơ chế X-Frame-Options: SAMEORIGIN khi hiển thị trực tiếp trong thẻ iframe.
   */
  const getEmbedUrl = (url) => {
    if (!url) return '';
    if (url.includes('youtube.com/embed/')) return url;
    const match = url.match(/(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?|shorts)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})/i);
    if (match && match[1]) {
      return `https://www.youtube.com/embed/${match[1]}`;
    }
    return url;
  };

  /**
   * Phân loại và gộp đoàn phim theo Đạo diễn và Diễn viên để hiển thị trực quan thông tin nhân sự.
   */
  const formatCrewMembers = (crewList) => {
    if (!crewList || crewList.length === 0) return 'Đang cập nhật';
    const directors = crewList.filter((c) => c.role === 'DIRECTOR').map((c) => c.name);
    const actors = crewList.filter((c) => c.role === 'ACTOR').map((c) => c.name);

    const parts = [];
    if (directors.length > 0) parts.push(`Đạo diễn: ${directors.join(', ')}`);
    if (actors.length > 0) parts.push(`Diễn viên: ${actors.join(', ')}`);
    return parts.length > 0 ? parts.join(' | ') : crewList.map((c) => c.name).join(', ');
  };

  return (
    <div className="screen-container">
      <h2 className="screen-title">Chi tiết thông tin phim</h2>

      {errorMessage && (
        <div className="alert-box alert-danger">
          <span>{errorMessage}</span>
          <button className="btn btn-gray" onClick={onNavigateBack}>Quay lại</button>
        </div>
      )}

      {loading && <div style={{ textAlign: 'center', margin: '20px 0', color: '#666' }}>Đang nạp thông tin phim...</div>}

      {movieInformation && (
        <div id="outMovieInformation" className="movie-detail-container">
          <div className="detail-full-row">
            <label className="form-label">Poster</label>
            <div className="detail-box poster-preview-wrapper" style={{ minHeight: 'auto', padding: '12px' }}>
              {movieInformation.poster ? (
                <img
                  src={movieInformation.poster}
                  alt={movieInformation.name}
                  className="poster-image"
                  onError={(e) => {
                    e.target.style.display = 'none';
                  }}
                />
              ) : (
                <div style={{ color: '#888' }}>&lt;chưa có link poster&gt;</div>
              )}
            </div>
          </div>

          <div className="detail-row">
            <div className="form-group">
              <label className="form-label">Mã phim</label>
              <div className="detail-box">
                {movieInformation.code || `P${String(movieInformation.id).padStart(3, '0')}`}
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Tên phim</label>
              <div className="detail-box" style={{ fontWeight: 600 }}>
                {movieInformation.name}
              </div>
            </div>
          </div>

          <div className="detail-full-row">
            <label className="form-label">Mô tả</label>
            <div className="detail-box" style={{ minHeight: '64px', lineHeight: '1.6' }}>
              {movieInformation.description || 'Không có mô tả.'}
            </div>
          </div>

          <div className="detail-full-row">
            <label className="form-label">Trailer</label>
            <div className="detail-box" style={{ padding: '12px' }}>
              {movieInformation.trailer ? (
                <div>
                  <div style={{ position: 'relative', width: '100%', maxWidth: '560px', aspectRatio: '16 / 9', margin: '0 0 8px 0' }}>
                    <iframe
                      src={getEmbedUrl(movieInformation.trailer)}
                      title={`Trailer phim ${movieInformation.name}`}
                      style={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        border: '1px solid #000',
                        borderRadius: '2px',
                      }}
                      allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                      allowFullScreen
                    />
                  </div>
                </div>
              ) : (
                <span style={{ color: '#888' }}>Chưa cập nhật trailer</span>
              )}
            </div>
          </div>

          <div className="detail-row">
            <div className="form-group">
              <label className="form-label">Ngôn ngữ</label>
              <div className="detail-box">
                {movieInformation.language === 'VIETNAMESE'
                  ? 'Tiếng Việt'
                  : movieInformation.language === 'ENGLISH'
                  ? 'Tiếng Anh'
                  : movieInformation.language === 'JAPANESE'
                  ? 'Tiếng Nhật'
                  : movieInformation.language || 'N/A'}
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Thể loại</label>
              <div className="detail-box">
                {movieInformation.categoryList && movieInformation.categoryList.length > 0
                  ? movieInformation.categoryList.map((c) => c.name).join(', ')
                  : 'N/A'}
              </div>
            </div>
          </div>

          <div className="detail-row">
            <div className="form-group">
              <label className="form-label">Thời lượng</label>
              <div className="detail-box">
                {movieInformation.runtime} phút
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Nhãn</label>
              <div className="detail-box">
                {movieInformation.movieLabel
                  ? `${movieInformation.movieLabel.code} (${movieInformation.movieLabel.description})`
                  : 'P (Phổ biến)'}
              </div>
            </div>
          </div>

          <div className="detail-full-row">
            <label className="form-label">Danh sách thành viên đoàn phim</label>
            <div className="detail-box">
              {formatCrewMembers(movieInformation.crewMemberList)}
            </div>
          </div>

          <div className="detail-row">
            <div className="form-group">
              <label className="form-label">Định dạng hỗ trợ</label>
              <div className="detail-box">
                {movieInformation.formatList && movieInformation.formatList.length > 0
                  ? movieInformation.formatList.map((f) => f.name).join(', ')
                  : '2D'}
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Loại bản dịch</label>
              <div className="detail-box">
                {movieInformation.transTypeList && movieInformation.transTypeList.length > 0
                  ? movieInformation.transTypeList.map((t) => t.name).join(', ')
                  : 'Sub'}
              </div>
            </div>
          </div>

          <div className="detail-row">
            <div className="form-group">
              <label className="form-label">Ngày khởi chiếu</label>
              <div className="detail-box">
                {formatDate(movieInformation.releaseDate)}
              </div>
            </div>

            <div className="form-group">
              <label className="form-label">Ngày dừng chiếu</label>
              <div className="detail-box">
                {formatDate(movieInformation.endDate)}
              </div>
            </div>
          </div>
        </div>
      )}

      <div className="form-bottom-actions">
        <button
          type="button"
          className="btn btn-pink"
          onClick={onNavigateBack}
        >
          Quay lại
        </button>

        <button
          type="button"
          className="btn btn-green"
          onClick={onNavigateBack}
        >
          Xác nhận
        </button>
      </div>
    </div>
  );
}

