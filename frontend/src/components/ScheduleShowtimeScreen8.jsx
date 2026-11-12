import React, { useState, useEffect } from 'react';
import { showtimeApi } from '../api/showtimeApi';

/**
 * Màn hình Form lập lịch chiếu mới, phục vụ việc chọn ngày chiếu, phim, khung giờ, định dạng,
 * bản dịch và phòng chiếu; tự động tính toán giờ kết thúc và kiểm tra các điều kiện ràng buộc/ngoại lệ.
 */
export default function ScheduleShowtimeScreen8({ onNavigateBack, onShowtimeCreated }) {
  const [movieList, setMovieList] = useState([]);
  const [timeSlotList, setTimeSlotList] = useState([]);
  const [screeningRoomList, setScreeningRoomList] = useState([]);

  const [selectedDate, setSelectedDate] = useState('');
  const [selectedSlotId, setSelectedSlotId] = useState('');
  const [selectedMovieId, setSelectedMovieId] = useState('');
  const [selectedFormatId, setSelectedFormatId] = useState('');
  const [selectedTransTypeId, setSelectedTransTypeId] = useState('');
  const [selectedRoomId, setSelectedRoomId] = useState('');

  const [calculatedEndTime, setCalculatedEndTime] = useState('');
  const [availableFormats, setAvailableFormats] = useState([]);
  const [availableTransTypes, setAvailableTransTypes] = useState([]);

  const [showCancelModal, setShowCancelModal] = useState(false);
  const [alertMessage, setAlertMessage] = useState({ type: '', text: '' });
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    getMovieList();
    getTimeSlotList();
  }, []);

  const getMovieList = async () => {
    try {
      const data = await showtimeApi.getMovieList();
      setMovieList(data);
      if (!data || data.length === 0) {
        setAlertMessage({
          type: 'warning',
          text: 'Không có phim nào trong danh sách đang chiếu (Luồng ngoại lệ 4)',
        });
      }
    } catch (err) {
      console.error('Lỗi khi tải danh sách phim:', err);
      setAlertMessage({ type: 'danger', text: 'Lỗi khi tải danh sách phim từ máy chủ.' });
    }
  };

  const getTimeSlotList = async () => {
    try {
      const data = await showtimeApi.getTimeSlotList();
      setTimeSlotList(data);
    } catch (err) {
      console.error('Lỗi khi tải danh sách khung giờ:', err);
    }
  };

  // Cập nhật danh sách định dạng và loại bản dịch tương ứng theo phim đã chọn
  useEffect(() => {
    if (!selectedMovieId) {
      setAvailableFormats([]);
      setAvailableTransTypes([]);
      setSelectedFormatId('');
      setSelectedTransTypeId('');
      setCalculatedEndTime('');
      return;
    }

    const movie = movieList.find((m) => String(m.id) === String(selectedMovieId));
    if (movie) {
      const formats = movie.formatList || [];
      const transTypes = movie.transTypeList || [];
      setAvailableFormats(formats);
      setAvailableTransTypes(transTypes);

      if (formats.length === 1) setSelectedFormatId(String(formats[0].id));
      else setSelectedFormatId('');

      if (transTypes.length === 1) setSelectedTransTypeId(String(transTypes[0].id));
      else setSelectedTransTypeId('');
    }
  }, [selectedMovieId, movieList]);

  // Tự động tính toán lại giờ kết thúc khi thay đổi khung giờ hoặc phim
  useEffect(() => {
    if (!selectedSlotId || !selectedMovieId) {
      setCalculatedEndTime('');
      return;
    }

    const slot = timeSlotList.find((s) => String(s.id) === String(selectedSlotId));
    const movie = movieList.find((m) => String(m.id) === String(selectedMovieId));

    if (slot && movie && slot.startTime && movie.runtime) {
      const endTime = calculateEndTime(slot.startTime, movie.runtime);
      setCalculatedEndTime(endTime);
    }
  }, [selectedSlotId, selectedMovieId, timeSlotList, movieList]);

  /**
   * Tính giờ kết thúc = Giờ bắt đầu + thời gian chuẩn bị (10 phút) + thời lượng phim + thời gian dọn rạp (15 phút)
   * Phục vụ quy tắc nghiệp vụ rạp chiếu: cộng thêm 25 phút đệm để đảm bảo có thời gian dọn rạp trước suất kế tiếp.
   */
  const calculateEndTime = (startTimeStr, runtimeMinutes) => {
    if (!startTimeStr) return '';
    const parts = startTimeStr.split(':');
    const startHour = parseInt(parts[0], 10);
    const startMinute = parseInt(parts[1], 10);

    const totalMinutes = startHour * 60 + startMinute + runtimeMinutes + 25;
    const endHour = Math.floor(totalMinutes / 60) % 24;
    const endMin = totalMinutes % 60;

    const formattedHour = String(endHour).padStart(2, '0');
    const formattedMin = String(endMin).padStart(2, '0');
    return `${formattedHour}:${formattedMin}:00`;
  };

  // Tải danh sách phòng chiếu thỏa mãn khi có đủ thông tin ngày, giờ, thời lượng và định dạng
  useEffect(() => {
    if (selectedDate && selectedSlotId && calculatedEndTime && selectedFormatId) {
      const slot = timeSlotList.find((s) => String(s.id) === String(selectedSlotId));
      if (slot && slot.startTime) {
        getScreeningRoomList(selectedDate, slot.startTime, calculatedEndTime, parseInt(selectedFormatId, 10));
      }
    } else {
      setScreeningRoomList([]);
      setSelectedRoomId('');
    }
  }, [selectedDate, selectedSlotId, calculatedEndTime, selectedFormatId, timeSlotList]);

  /**
   * Lọc động phòng chiếu: chỉ lấy phòng đang ACTIVE, hỗ trợ định dạng của phim và chưa bị trùng
   * lịch chiếu trong khoảng [startTime, endTime]. Nếu không có phòng, cảnh báo theo luồng ngoại lệ 7.
   */
  const getScreeningRoomList = async (showtime, startTime, endTime, formatId) => {
    try {
      const rooms = await showtimeApi.getScreeningRoomList(showtime, startTime, endTime, formatId);
      setScreeningRoomList(rooms);

      if (!rooms || rooms.length === 0) {
        setAlertMessage({
          type: 'warning',
          text: 'Không có phòng chiếu nào trống thỏa mãn điều kiện thời gian và định dạng',
        });
      } else {
        if (alertMessage.text.includes('Không có phòng chiếu')) {
          setAlertMessage({ type: '', text: '' });
        }
      }
    } catch (err) {
      console.error('Lỗi khi lấy danh sách phòng chiếu:', err);
    }
  };

  /**
   * Kiểm tra không cho phép lên lịch chiếu ở thời điểm quá khứ, phục vụ luồng ngoại lệ 9 trong đặc tả.
   */
  const validateNotPastDateTime = (dateStr, timeStr) => {
    if (!dateStr) return true;
    const now = new Date();
    const todayStr = now.toISOString().split('T')[0];

    if (dateStr < todayStr) {
      return false;
    }
    if (dateStr === todayStr && timeStr) {
      const currentHour = now.getHours();
      const currentMin = now.getMinutes();
      const parts = timeStr.split(':');
      const startHour = parseInt(parts[0], 10);
      const startMin = parseInt(parts[1], 10);

      if (startHour < currentHour || (startHour === currentHour && startMin < currentMin)) {
        return false;
      }
    }
    return true;
  };

  const handleConfirmSubmit = async () => {
    if (!selectedDate || !selectedSlotId || !selectedMovieId || !selectedFormatId || !selectedTransTypeId || !selectedRoomId) {
      setAlertMessage({
        type: 'danger',
        text: 'Vui lòng điền và chọn đầy đủ thông tin: Ngày chiếu, Phim, Khung giờ, Định dạng, Bản dịch, Phòng chiếu!',
      });
      return;
    }

    const slot = timeSlotList.find((s) => String(s.id) === String(selectedSlotId));
    const movie = movieList.find((m) => String(m.id) === String(selectedMovieId));
    const format = availableFormats.find((f) => String(f.id) === String(selectedFormatId));
    const transType = availableTransTypes.find((t) => String(t.id) === String(selectedTransTypeId));
    const room = screeningRoomList.find((r) => String(r.id) === String(selectedRoomId));

    if (!validateNotPastDateTime(selectedDate, slot?.startTime)) {
      setAlertMessage({
        type: 'danger',
        text: 'Thời gian chiếu đã qua',
      });
      return;
    }

    const newShowtime = {
      showtime: selectedDate,
      endTime: calculatedEndTime,
      timeSlot: slot,
      screeningRoom: room,
      transType: transType,
      format: format,
      movie: movie,
    };

    await scheduleShowtime(newShowtime);
  };

  const scheduleShowtime = async (newShowtime) => {
    try {
      setSubmitting(true);
      setAlertMessage({ type: '', text: '' });
      await showtimeApi.createScheduleShowtime(newShowtime);

      alert('Lên lịch chiếu thành công!');
      if (onShowtimeCreated) {
        onShowtimeCreated();
      } else if (onNavigateBack) {
        onNavigateBack();
      }
    } catch (err) {
      console.error('Lỗi khi lên lịch chiếu:', err);
      setAlertMessage({
        type: 'danger',
        text: err.message || 'Lỗi khi lưu lịch chiếu vào cơ sở dữ liệu!',
      });
    } finally {
      setSubmitting(false);
    }
  };

  /**
   * Hiển thị hộp thoại xác nhận hủy bỏ thao tác, phục vụ luồng ngoại lệ 3 nhằm bảo vệ
   * dữ liệu đang nhập không bị mất ngoài ý muốn khi người dùng bấm Quay lại.
   */
  const handleCancelClick = () => {
    setShowCancelModal(true);
  };

  const handleConfirmCancel = () => {
    setShowCancelModal(false);
    if (onNavigateBack) onNavigateBack();
  };

  // Price calculations for summary display (steps 9-10 in docx)
  const selectedSlot = timeSlotList.find((s) => String(s.id) === String(selectedSlotId));
  const selectedMovie = movieList.find((m) => String(m.id) === String(selectedMovieId));
  const selectedFormat = availableFormats.find((f) => String(f.id) === String(selectedFormatId));

  const basePrice = selectedSlot?.basePrice || 0;
  const movieCharge = selectedMovie?.movieCharge || 0;
  const formatCharge = selectedFormat?.formatCharge || 0;
  const totalEstimatedPrice = Number(basePrice) + Number(movieCharge) + Number(formatCharge);

  const formatCurrency = (val) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(val || 0);
  };

  return (
    <div className="screen-container">
      {/* Title matching Image 2: Lên lịch chiếu */}
      <h2 className="screen-title">Lên lịch chiếu</h2>

      {/* Alert Box for validation and exception messages */}
      {alertMessage.text && (
        <div className={`alert-box alert-${alertMessage.type}`}>
          <span>{alertMessage.text}</span>
          <button className="btn btn-gray" onClick={() => setAlertMessage({ type: '', text: '' })}>Đóng</button>
        </div>
      )}

      <div className="form-two-column">
        <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
          <div className="form-group">
            <label className="form-label">Ngày chiếu</label>
            <input
              id="inoutShowtime"
              type="date"
              className="wireframe-input"
              value={selectedDate}
              onChange={(e) => {
                setSelectedDate(e.target.value);
                setAlertMessage({ type: '', text: '' });
              }}
              placeholder="<chọn ngày chiếu>"
            />
          </div>

          <div className="form-group">
            <label className="form-label">Chọn phim</label>
            <select
              id="inoutMovies"
              className="wireframe-select"
              value={selectedMovieId}
              onChange={(e) => setSelectedMovieId(e.target.value)}
            >
              <option value="">&lt;chọn phim&gt;</option>
              {movieList.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.code ? `[${m.code}] ` : ''}{m.name} ({m.runtime} phút)
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">Chọn loại bản dịch</label>
            <select
              id="inoutTransTypes"
              className="wireframe-select"
              value={selectedTransTypeId}
              onChange={(e) => setSelectedTransTypeId(e.target.value)}
              disabled={availableTransTypes.length === 0}
            >
              <option value="">&lt;chọn loại bản dịch&gt;</option>
              {availableTransTypes.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.name}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div style={{ display: 'flex', flexDirection: 'column', gap: '28px' }}>
          <div className="form-group">
            <label className="form-label">Khung giờ</label>
            <select
              id="inoutTimeSlots"
              className="wireframe-select"
              value={selectedSlotId}
              onChange={(e) => setSelectedSlotId(e.target.value)}
            >
              <option value="">&lt;chọn khung giờ&gt;</option>
              {timeSlotList.map((s) => (
                <option key={s.id} value={s.id}>
                  {s.name} ({s.startTime?.substring(0, 5)}) - {formatCurrency(s.basePrice)}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">Chọn định dạng</label>
            <select
              id="inoutFormats"
              className="wireframe-select"
              value={selectedFormatId}
              onChange={(e) => setSelectedFormatId(e.target.value)}
              disabled={availableFormats.length === 0}
            >
              <option value="">&lt;chọn định dạng&gt;</option>
              {availableFormats.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.name} {f.formatCharge > 0 ? `(+${formatCurrency(f.formatCharge)})` : ''}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label className="form-label">Chọn phòng chiếu</label>
            <select
              id="inoutScreeningRooms"
              className="wireframe-select"
              value={selectedRoomId}
              onChange={(e) => setSelectedRoomId(e.target.value)}
              disabled={screeningRoomList.length === 0}
            >
              <option value="">&lt;chọn phòng chiếu&gt;</option>
              {screeningRoomList.map((r) => (
                <option key={r.id} value={r.id}>
                  {r.name} ({r.rowNumber * r.colNumber} ghế)
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      {/* Bảng hiển thị thông tin tính toán tự động (giờ kết thúc & dự tính giá vé) */}
      {(calculatedEndTime || selectedSlotId || selectedMovieId) && (
        <div className="calc-summary-panel">
          <div>
            <div className="calc-item-label">Giờ bắt đầu:</div>
            <div className="calc-item-value">{selectedSlot?.startTime?.substring(0, 5) || '--:--'}</div>
          </div>
          <div>
            <div className="calc-item-label">Thời lượng phim:</div>
            <div className="calc-item-value">{selectedMovie?.runtime ? `${selectedMovie.runtime} phút` : '--'}</div>
          </div>
          <div>
            <div className="calc-item-label">Giờ kết thúc dự kiến:</div>
            <span id="outEndTime" className="calc-item-value" style={{ color: '#0284c7' }}>
              {calculatedEndTime ? calculatedEndTime.substring(0, 5) : '--:--'}
            </span>
          </div>
          <div>
            <div className="calc-item-label">Giá cơ sở:</div>
            <div className="calc-item-value">{formatCurrency(basePrice)}</div>
          </div>
          <div>
            <div className="calc-item-label">Phụ thu định dạng:</div>
            <div className="calc-item-value">{formatCurrency(formatCharge)}</div>
          </div>
          <div>
            <div className="calc-item-label">Phụ thu phim:</div>
            <div className="calc-item-value">{formatCurrency(movieCharge)}</div>
          </div>
          <div>
            <div className="calc-item-label">Tổng vé tối thiểu:</div>
            <div className="calc-item-value" style={{ color: '#16a34a' }}>
              {formatCurrency(totalEstimatedPrice)}
            </div>
          </div>
        </div>
      )}

      <div className="form-bottom-actions">
        <button
          id="subCancel"
          type="button"
          className="btn btn-pink"
          onClick={handleCancelClick}
        >
          Quay lại
        </button>

        <button
          id="subScheduleShowtime"
          type="button"
          className="btn btn-green"
          onClick={handleConfirmSubmit}
          disabled={submitting}
        >
          {submitting ? 'Đang lưu...' : 'Xác nhận'}
        </button>
      </div>

      {/* Confirmation Modal for Luồng ngoại lệ 3 */}
      {showCancelModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <h3 className="modal-title">Xác nhận</h3>
            <p>Bạn có chắc chắn muốn hủy?</p>
            <div className="modal-actions">
              <button
                type="button"
                className="btn btn-gray"
                onClick={() => setShowCancelModal(false)}
              >
                Không
              </button>
              <button
                type="button"
                className="btn btn-pink"
                onClick={handleConfirmCancel}
              >
                Đồng ý
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
