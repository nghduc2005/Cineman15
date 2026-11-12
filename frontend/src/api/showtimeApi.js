const BASE_URL = '/api';

export const showtimeApi = {
  // Showtime API
  getScheduleShowtimeList: async () => {
    const res = await fetch(`${BASE_URL}/showtimes`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },

  createScheduleShowtime: async (newShowtime) => {
    const res = await fetch(`${BASE_URL}/showtimes`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newShowtime),
    });
    const data = await res.json();
    if (!res.ok) {
      throw new Error(data.message || 'Lỗi khi tạo lịch chiếu');
    }
    return data;
  },

  createScheduleShowTime: async (newShowtime) => {
    return showtimeApi.createScheduleShowtime(newShowtime);
  },

  // Movie API
  getMovieList: async () => {
    const res = await fetch(`${BASE_URL}/movies`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },

  getMovieBySearchKeyword: async (keyword) => {
    const param = keyword !== undefined && keyword !== null ? `?keyword=${encodeURIComponent(keyword)}` : '';
    const res = await fetch(`${BASE_URL}/movies/search${param}`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },

  getMovieInformation: async (id) => {
    const res = await fetch(`${BASE_URL}/movies/${id}`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },

  // TimeSlot API
  getTimeSlotList: async () => {
    const res = await fetch(`${BASE_URL}/timeslots`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },

  // ScreeningRoom API
  getScreeningRoomList: async (showtime, startTime, endTime, formatId) => {
    let url = `${BASE_URL}/screeningrooms`;
    const params = new URLSearchParams();
    if (showtime) params.append('showtime', showtime);
    if (startTime) params.append('startTime', startTime);
    if (endTime) params.append('endTime', endTime);
    if (formatId) params.append('formatId', formatId);
    if (params.toString()) {
      url += `?${params.toString()}`;
    }
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  },
};
