# Hệ Thống Quản Lý Rạp Chiếu Phim (CineMan) - PTTK BTL Nhóm 8

Dự án được xây dựng và triển khai khớp 100% với tài liệu đặc tả, phân tích & thiết kế hệ thống **`15B23DCCN178.docx`**, tập trung thực hiện hoàn chỉnh 2 phân hệ nghiệp vụ chính:
1. **Module 1: Lên lịch chiếu (Schedule Showtime)**
2. **Module 2: Tìm kiếm thông tin phim (Search Movie Information)**
Kèm theo toàn bộ các kịch bản ngoại lệ (Exception flows) và giao diện tối giản theo đúng bản vẽ wireframe mẫu.

---

## 1. Ánh Xạ Kiến Trúc & Sơ Đồ Lớp (UML Class Diagram Mapping)

### 1.1. Lớp Biên / Giao Diện (React Frontend)
Giao diện được thiết kế theo đúng wireframe mẫu (giao diện bảng ô lưới tối giản, nút màu đặc trưng):

| Giao diện / File | Thuộc tính (Attributes) | Phương thức / Hành động (Operations) |
| :--- | :--- | :--- |
| **`ShowtimeManageScreen8.jsx`**<br>*(Màn hình Quản lý lịch chiếu)* | `- subScheduleShowtime : AnchorHTML`<br>`- showtimeList : Showtime8[]` | `+ getShowtimeList() : Showtime8[]`<br>`+ setShowtimeList(showtimeList : Showtime8[]) : void` |
| **`ScheduleShowtimeScreen8.jsx`**<br>*(Màn hình Lên lịch chiếu)* | `- inoutShowtime : InputHTML`<br>`- inoutTimeSlots : SelectHTML`<br>`- inoutMovies : SelectHTML`<br>`- inoutFormats : SelectHTML`<br>`- inoutScreeningRooms : SelectHTML`<br>`- inoutTransTypes : SelectHTML`<br>`- outEndTime : SpanHTML`<br>`- subScheduleShowtime : ButtonHTML`<br>`- subCancel : ButtonHTML`<br>`- movieList : Movie8[]`<br>`- timeSlotList : TimeSlot8[]`<br>`- screeningRoomList : ScreeningRoom8[]` | `+ calculateEndTime(startTime : LocalTime, runtime : int) : LocalTime`<br>`+ getMovieList() : Movie8[]`<br>`+ getTimeSlotList() : TimeSlot8[]`<br>`+ getScreeningRoomList(showtime : Date, startTime : LocalTime, endTime : LocalTime, formatId : int) : ScreeningRoom8[]`<br>`+ setMovieList(movieList : Movie8[]) : void`<br>`+ setTimeSlotList(timeSlotList : TimeSlot8[]) : void`<br>`+ setScreeningRoomList(screeningRoomList : ScreeningRoom8[]) : void`<br>`+ scheduleShowtime(newShowtime : Showtime8) : void` |
| **`SearchMovieInformationScreen8.jsx`**<br>*(Màn hình Tìm kiếm phim)* | `- inKeyword : HTMLInput`<br>`- outMovieResult : HTMLTable`<br>`- subSearch : HTMLButton`<br>`- movieList : Movie8[]` | `+ getMovieList() : Movie8[]`<br>`+ getMovieBySearchKeyword(keyword : string) : Movie8[]`<br>`+ setMovieList(movieList : Movie8[]) : void` |
| **`MovieInformationScreen8.jsx`**<br>*(Màn hình Chi tiết phim)* | `- outMovieInformation : HTMLDiv`<br>`- movieInformation : Movie8` | `+ getMovieInformation(id : int) : Movie8`<br>`+ setMovieInformation(movie : Movie8) : void` |

---

### 1.2. Lớp Điều Khiển (Spring Boot REST Controllers)
Toàn bộ các lớp Controller đều mang hậu tố `8` đúng theo chuẩn sơ đồ lớp:
- **`ShowtimeController8`** (`/api/showtimes`):
  - `POST /api/showtimes`: `createScheduleShowTime(@RequestBody Showtime8 newShowtime) : ResponseEntity`
  - `GET /api/showtimes`: `getScheduleShowtimeList() : ResponseEntity<List<Showtime8>>`
- **`MovieController8`** (`/api/movies`):
  - `GET /api/movies`: `getMovieList() : ResponseEntity<List<Movie8>>`
  - `GET /api/movies/search?keyword={keyword}`: `getMovieBySearchKeyword(keyword) : ResponseEntity<List<Movie8>>`
  - `GET /api/movies/{id}`: `getMovieInformation(id) : ResponseEntity<Movie8>`
- **`TimeSlotController8`** (`/api/timeslots`):
  - `GET /api/timeslots`: `getTimeSlotList() : ResponseEntity<List<TimeSlot8>>`
- **`ScreeningRoomController8`** (`/api/screeningrooms`):
  - `GET /api/screeningrooms`: `getScreeningRoomList(showtime, startTime, endTime, formatId) : ResponseEntity<List<ScreeningRoom8>>`

---

### 1.3. Lớp Dữ Liệu & Thực Thể (Entities & Repositories kế thừa BaseRepository8)
- **`BaseRepository8`** (Lớp cơ sở DAO chuẩn JDBC theo giáo trình PTTK):
  - Thuộc tính: `#conn : Connection` (đối tượng kết nối CSDL dùng chung ở mức `protected` để các lớp con kế thừa).
  - Phương thức: `+BaseRepository8()` (khởi tạo kết nối CSDL JDBC), `+closeConnection() : void`, `+getConnection() : Connection`.
- **Các lớp Repository cụ thể đều kết thúc bằng `8` và kế thừa từ `BaseRepository8`**:
  - `ShowtimeRepository8 extends BaseRepository8`: `createScheduleShowtime()`, `getScheduleShowtimeList()`, `findConflictingShowtimes()`.
  - `MovieRepository8 extends BaseRepository8`: `getMovieList()`, `getMovieBySearchKeyword()`, `getMovieInformation()`.
  - `TimeSlotRepository8 extends BaseRepository8`: `getTimeSlotList()`.
  - `ScreeningRoomRepository8 extends BaseRepository8`: `getScreeningRoomList()`, `findByStatus()`.
  - `CategoryRepository8`, `CrewMemberRepository8`, `FormatRepository8`, `MovieLabelRepository8`, `SeatRepository8`, `SeatTierRepository8`, `TransTypeRepository8`.
- **Toàn bộ Lớp Thực Thể (Domain Entities & Enums)**:
  - `Showtime8`, `Movie8`, `ScreeningRoom8`, `TimeSlot8`, `Format8`, `TransType8`, `Category8`, `CrewMember8`, `MovieLabel8`, `Seat8`, `SeatTier8`.
  - Enums: `CrewRole8`, `MovieLanguage8`, `RoomStatus8`, `SeatStatus8`.
- **Cấu hình & Điểm khởi chạy**:
  - `CinemaApplication8` (Lớp khởi chạy Spring Boot chính).
  - `DataInitializer8` (Lớp khởi tạo dữ liệu mẫu).
  - `WebConfig8` (Cấu hình CORS).

---

## 2. Xử Lý Đầy Đủ Các Luồng Ngoại Lệ (Exception Flows)

### 2.1. Module Lên Lịch Chiếu
1. **Ngoại lệ 1 (Hủy thao tác)**:
   - Khi nhấn nút `Quay lại`, hệ thống hiển thị hộp thoại xác nhận: *"Bạn có chắc chắn muốn hủy?"*.
   - Chọn "Đồng ý": Xóa dữ liệu tạm và chuyển hướng về màn hình Quản lý lịch chiếu.
   - Chọn "Không": Đóng hộp thoại và giữ nguyên trạng thái biểu mẫu hiện tại.
2. **Ngoại lệ 2 (Danh sách phim trống)**:
   - Hệ thống cảnh báo: *"Không có phim nào trong danh sách đang chiếu"*.
3. **Ngoại lệ 3 (Thời gian chiếu đã qua)**:
   - Nếu người dùng chọn ngày chiếu hoặc khung giờ trong quá khứ so với thời gian hiện tại: Hệ thống cảnh báo lỗi *"Thời gian chiếu đã qua"*.
4. **Ngoại lệ 4 (Không có phòng rảnh thỏa mãn)**:
   - Khi chọn ngày, khung giờ, định dạng nhưng không có phòng nào phù hợp hoặc phòng bị trùng lịch: Hệ thống hiển thị cảnh báo: *"Không có phòng chiếu nào trống thỏa mãn điều kiện thời gian và định dạng"*.
5. **Ngoại lệ 5 (Xung đột lịch chiếu khi lưu)**:
   - Backend kiểm tra trùng phòng trong khoảng thời gian `[startTime, endTime]` trên cơ sở dữ liệu. Nếu trùng, trả về mã lỗi `409 CONFLICT` cùng thông báo: *"Phòng chiếu đã có lịch chiếu trong khung giờ này!"*.
6. **Công thức tính giờ kết thúc**:
   - `Giờ kết thúc = Giờ bắt đầu + thời gian chuẩn bị (10 phút) + thời lượng phim + thời gian dọn phòng (15 phút)` (Tổng cộng: `startTime + runtime + 25 phút` theo đúng dòng 502 trong docx).

### 2.2. Module Tìm Kiếm Thông Tin Phim
1. **Ngoại lệ 1 (Từ khóa rỗng)**:
   - Khách hàng không nhập gì nhưng vẫn nhấn Enter hoặc nút Tìm kiếm: Hệ thống tự động truy vấn trên toàn bộ danh sách phim đang trong thời gian chiếu, sắp xếp theo thứ tự từ điển A-Z.
2. **Ngoại lệ 2 (Không tìm thấy kết quả phù hợp)**:
   - Khi từ khóa không khớp với phim nào trong CSDL: Hệ thống hiển thị thông báo: *"Không tìm thấy phim phù hợp"*.
3. **Ngoại lệ 3 (Lỗi tải chi tiết phim)**:
   - Khi truy cập phim không tồn tại: Hệ thống hiển thị thông báo lỗi và cho phép quay lại danh sách tìm kiếm.

---

## 3. Hướng Dẫn Khởi Chạy Hệ Thống

### 3.1. Chạy Toàn Hệ Thống Bằng 1 Command Duy Nhất (Khuyên Dùng)
Tại thư mục gốc dự án (`d:\PTIT\PTTK\BTL\Code`), bạn có thể dùng **1 trong các cách sau** để chạy đồng thời cả Backend Spring Boot, Frontend React và tự động mở trình duyệt:

- **Cách 1 (CMD / Double-click)**:
  ```cmd
  .\start.bat
  ```
  *(hoặc nhấp đúp trực tiếp vào file `start.bat`)*

- **Cách 2 (PowerShell)**:
  ```powershell
  .\start.ps1
  ```

- **Cách 3 (npm)**:
  ```bash
  npm start
  ```

---

### 3.2. Khởi Chạy Thủ Công Từng Phân Hệ (Tùy chọn)

#### Khởi chạy Backend (Spring Boot REST API)
1. Mở terminal tại thư mục `backend/`:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
2. Server backend sẽ khởi chạy tại: `http://localhost:8080`.
3. Giao diện quản trị CSDL H2 Console (tùy chọn):
   - URL: `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:cinemadb`
   - Username: `sa` | Password: *(để trống)*

#### Khởi chạy Frontend (React Vite)
1. Mở terminal tại thư mục `frontend/`:
   ```bash
   cd frontend
   npm run dev
   ```
2. Mở trình duyệt tại: `http://localhost:5173`.

---

## 4. Dữ Liệu Mẫu Kiểm Thử Tích Hợp (Đúng 100% Theo Docx)
Hệ thống tự động khởi tạo dữ liệu mẫu khi khởi động:
- **P001**: Lật Mặt 7: Một Điều Ước (138 phút, 2D, Sub)
- **P002**: Doraemon: Bản Giao Hưởng Địa Cầu (115 phút, 2D, Sub & Dub)
- **P003**: Avatar: Dòng Chảy Của Nước (192 phút, 2D, 3D, IMAX, Sub & Dub, Đạo diễn: James Cameron, Diễn viên: Sam Worthington, Zoe Saldaña, Sigourney Weaver)
- **Phòng chiếu**:
  - Phòng số 1: Hỗ trợ 2D
  - Phòng số 2: Hỗ trợ 2D, 3D, IMAX
  - Phòng số 3 (IMAX Laser): Hỗ trợ 2D, IMAX
  - Phòng số 4: Trạng thái đang bảo trì (MAINTENANCE)
