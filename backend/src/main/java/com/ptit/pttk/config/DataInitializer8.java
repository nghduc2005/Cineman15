package com.ptit.pttk.config;

import com.ptit.pttk.model.*;
import com.ptit.pttk.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Khởi tạo dữ liệu mẫu ban đầu theo kịch bản bài tập lớn PTTK (bao gồm các phim P001, P002, P003,
 * danh mục phòng chiếu, định dạng, khung giờ và lịch chiếu sẵn) nhằm phục vụ kiểm thử ngay các
 * luồng nghiệp vụ tìm kiếm thông tin phim và lên lịch chiếu mà không cần nhập liệu thủ công.
 */
@Component
@Transactional
public class DataInitializer8 implements CommandLineRunner {

    private final MovieLabelRepository8 movieLabelRepository8;
    private final CategoryRepository8 categoryRepository8;
    private final FormatRepository8 formatRepository8;
    private final TransTypeRepository8 transTypeRepository8;
    private final TimeSlotRepository8 timeSlotRepository8;
    private final ScreeningRoomRepository8 screeningRoomRepository8;
    private final SeatTierRepository8 seatTierRepository8;
    private final SeatRepository8 seatRepository8;
    private final CrewMemberRepository8 crewMemberRepository8;
    private final MovieRepository8 movieRepository8;
    private final ShowtimeRepository8 showtimeRepository8;

    @Autowired
    public DataInitializer8(MovieLabelRepository8 movieLabelRepository8,
                            CategoryRepository8 categoryRepository8,
                            FormatRepository8 formatRepository8,
                            TransTypeRepository8 transTypeRepository8,
                            TimeSlotRepository8 timeSlotRepository8,
                            ScreeningRoomRepository8 screeningRoomRepository8,
                            SeatTierRepository8 seatTierRepository8,
                            SeatRepository8 seatRepository8,
                            CrewMemberRepository8 crewMemberRepository8,
                            MovieRepository8 movieRepository8,
                            ShowtimeRepository8 showtimeRepository8) {
        this.movieLabelRepository8 = movieLabelRepository8;
        this.categoryRepository8 = categoryRepository8;
        this.formatRepository8 = formatRepository8;
        this.transTypeRepository8 = transTypeRepository8;
        this.timeSlotRepository8 = timeSlotRepository8;
        this.screeningRoomRepository8 = screeningRoomRepository8;
        this.seatTierRepository8 = seatTierRepository8;
        this.seatRepository8 = seatRepository8;
        this.crewMemberRepository8 = crewMemberRepository8;
        this.movieRepository8 = movieRepository8;
        this.showtimeRepository8 = showtimeRepository8;
    }

    @Override
    public void run(String... args) throws Exception {
        if (movieRepository8.count() > 0) {
            return; // Data already initialized
        }

        MovieLabel8 labelP = movieLabelRepository8.save(new MovieLabel8(null, "P", "Phim được phép phổ biến đến người xem ở mọi độ tuổi"));
        MovieLabel8 labelT13 = movieLabelRepository8.save(new MovieLabel8(null, "T13", "Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên"));
        MovieLabel8 labelT16 = movieLabelRepository8.save(new MovieLabel8(null, "T16", "Phim được phổ biến đến người xem từ đủ 16 tuổi trở lên"));
        MovieLabel8 labelT18 = movieLabelRepository8.save(new MovieLabel8(null, "T18", "Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên"));

        Category8 catAction = categoryRepository8.save(new Category8("Hành động"));
        Category8 catSciFi = categoryRepository8.save(new Category8("Khoa học viễn tưởng"));
        Category8 catDrama = categoryRepository8.save(new Category8("Tâm lý - Xã hội"));
        Category8 catAnimation = categoryRepository8.save(new Category8("Hoạt hình"));
        Category8 catHorror = categoryRepository8.save(new Category8("Kinh dị - Giật gân"));

        Format8 fmt2D = formatRepository8.save(new Format8("2D", BigDecimal.valueOf(0)));
        Format8 fmt3D = formatRepository8.save(new Format8("3D", BigDecimal.valueOf(25000)));
        Format8 fmtIMAX = formatRepository8.save(new Format8("IMAX", BigDecimal.valueOf(30000)));
        Format8 fmtIMAX2D = formatRepository8.save(new Format8("IMAX 2D", BigDecimal.valueOf(50000)));
        Format8 fmtIMAX3D = formatRepository8.save(new Format8("IMAX 3D", BigDecimal.valueOf(75000)));

        TransType8 transSub = transTypeRepository8.save(new TransType8("Sub"));
        TransType8 transDub = transTypeRepository8.save(new TransType8("Dub"));
        TransType8 transVoice = transTypeRepository8.save(new TransType8("Thuyết minh"));

        TimeSlot8 slot1 = timeSlotRepository8.save(new TimeSlot8("Suất sáng 09:00", LocalTime.of(9, 0), BigDecimal.valueOf(75000)));
        TimeSlot8 slot2 = timeSlotRepository8.save(new TimeSlot8("Suất trưa 11:30", LocalTime.of(11, 30), BigDecimal.valueOf(80000)));
        TimeSlot8 slot3 = timeSlotRepository8.save(new TimeSlot8("Suất chiều 14:15", LocalTime.of(14, 15), BigDecimal.valueOf(85000)));
        TimeSlot8 slot4 = timeSlotRepository8.save(new TimeSlot8("Suất chiều tối 17:00", LocalTime.of(17, 0), BigDecimal.valueOf(95000)));
        TimeSlot8 slot5 = timeSlotRepository8.save(new TimeSlot8("Suất tối 19:00", LocalTime.of(19, 0), BigDecimal.valueOf(85000)));
        TimeSlot8 slot6 = timeSlotRepository8.save(new TimeSlot8("Suất đêm 21:45", LocalTime.of(21, 45), BigDecimal.valueOf(90000)));

        ScreeningRoom8 room1 = new ScreeningRoom8("ROOM_01", "Phòng số 1", 8, 10, RoomStatus8.ACTIVE);
        room1.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D)));
        room1 = screeningRoomRepository8.save(room1);

        ScreeningRoom8 room2 = new ScreeningRoom8("ROOM_02", "Phòng số 2", 6, 8, RoomStatus8.ACTIVE);
        room2.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmt3D, fmtIMAX)));
        room2 = screeningRoomRepository8.save(room2);

        ScreeningRoom8 room3 = new ScreeningRoom8("ROOM_03", "Phòng số 3 (IMAX Laser)", 10, 14, RoomStatus8.ACTIVE);
        room3.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmtIMAX, fmtIMAX2D, fmtIMAX3D)));
        room3 = screeningRoomRepository8.save(room3);

        ScreeningRoom8 room4 = new ScreeningRoom8("ROOM_04", "Phòng số 4 (Đang bảo trì)", 8, 10, RoomStatus8.MAINTENANCE);
        room4 = screeningRoomRepository8.save(room4);

        SeatTier8 tierStandard = seatTierRepository8.save(new SeatTier8("Ghế thường", BigDecimal.valueOf(0)));
        SeatTier8 tierVIP = seatTierRepository8.save(new SeatTier8("Ghế VIP", BigDecimal.valueOf(15000)));
        SeatTier8 tierSweetbox = seatTierRepository8.save(new SeatTier8("Ghế Đôi (Sweetbox)", BigDecimal.valueOf(35000)));

        List<Seat8> seats = new ArrayList<>();
        String[] rows = {"A", "B", "C", "D", "E", "F", "G", "H"};
        for (int r = 0; r < rows.length; r++) {
            SeatTier8 tier = (r < 2) ? tierStandard : ((r < 6) ? tierVIP : tierSweetbox);
            for (int c = 1; c <= 10; c++) {
                seats.add(new Seat8(rows[r], c, SeatStatus8.ACTIVE, tier, room1));
            }
        }
        seatRepository8.saveAll(seats);

        CrewMember8 nolan = crewMemberRepository8.save(new CrewMember8("Christopher Nolan", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", CrewRole8.DIRECTOR));
        CrewMember8 cillian = crewMemberRepository8.save(new CrewMember8("Cillian Murphy", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200", CrewRole8.ACTOR));
        CrewMember8 villeneuve = crewMemberRepository8.save(new CrewMember8("Denis Villeneuve", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200", CrewRole8.DIRECTOR));
        CrewMember8 timothee = crewMemberRepository8.save(new CrewMember8("Timothée Chalamet", "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=200", CrewRole8.ACTOR));
        CrewMember8 zendaya = crewMemberRepository8.save(new CrewMember8("Zendaya", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", CrewRole8.ACTOR));
        CrewMember8 cameron = crewMemberRepository8.save(new CrewMember8("James Cameron", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200", CrewRole8.DIRECTOR));
        CrewMember8 sam = crewMemberRepository8.save(new CrewMember8("Sam Worthington", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200", CrewRole8.ACTOR));
        CrewMember8 zoe = crewMemberRepository8.save(new CrewMember8("Zoe Saldaña", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", CrewRole8.ACTOR));
        CrewMember8 sigourney = crewMemberRepository8.save(new CrewMember8("Sigourney Weaver", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200", CrewRole8.ACTOR));

        // Phim P001, P002, P003 theo kịch bản bài tập lớn
        Movie8 movieLatMat = new Movie8(
            null,
            "Lật Mặt 7: Một Điều Ước",
            "Câu chuyện gia đình bà Hai 73 tuổi sống một mình và những đứa con ở xa, một tác phẩm cảm động về tình mẫu tử và gắn kết gia đình.",
            "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=d_k8qF5HwEQ",
            BigDecimal.valueOf(0),
            MovieLanguage8.VIETNAMESE,
            LocalDate.of(2026, 8, 1),
            LocalDate.of(2026, 10, 30),
            138,
            labelP
        );
        movieLatMat.setCode("P001");
        movieLatMat.setCategoryList(new java.util.HashSet<>(Arrays.asList(catDrama)));
        movieLatMat.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D)));
        movieLatMat.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub)));
        movieLatMat = movieRepository8.save(movieLatMat);

        Movie8 movieDoraemon = new Movie8(
            null,
            "Doraemon: Bản Giao Hưởng Địa Cầu",
            "Doraemon và nhóm bạn tham gia vào chuyến phiêu lưu âm nhạc kỳ thú để giải cứu Trái Đất khỏi hiểm họa diệt vong.",
            "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=420G7vXoB1I",
            BigDecimal.valueOf(0),
            MovieLanguage8.JAPANESE,
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 10, 15),
            115,
            labelP
        );
        movieDoraemon.setCode("P002");
        movieDoraemon.setCategoryList(new java.util.HashSet<>(Arrays.asList(catAnimation, catSciFi)));
        movieDoraemon.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D)));
        movieDoraemon.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub, transDub)));
        movieDoraemon = movieRepository8.save(movieDoraemon);

        Movie8 movieAvatar = new Movie8(
            null,
            "Avatar: Dòng Chảy Của Nước",
            "Câu chuyện về gia đình Sully và những rắc rối theo sau họ trên hành tinh Pandora. Họ phải sát cánh cùng nhau để bảo vệ sự sinh tồn của bộ tộc.",
            "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=d9MyW72ELq0",
            BigDecimal.valueOf(15000),
            MovieLanguage8.ENGLISH,
            LocalDate.of(2026, 8, 20),
            LocalDate.of(2026, 11, 30),
            192,
            labelT13
        );
        movieAvatar.setCode("P003");
        movieAvatar.setCategoryList(new java.util.HashSet<>(Arrays.asList(catAction, catSciFi)));
        movieAvatar.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmt3D, fmtIMAX)));
        movieAvatar.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub, transDub)));
        movieAvatar.setCrewMemberList(new java.util.HashSet<>(Arrays.asList(cameron, sam, zoe, sigourney)));
        movieAvatar = movieRepository8.save(movieAvatar);

        Movie8 movieDune = new Movie8(
            null,
            "Dune: Hành Tinh Cát - Phần 2",
            "Paul Atreides hợp lực cùng Chani và tộc Fremen để trả thù những kẻ đã hủy hoại gia đình mình, đồng thời đối mặt với lựa chọn giữa tình yêu và số mệnh.",
            "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=Way9Dexny3w",
            BigDecimal.valueOf(15000),
            MovieLanguage8.ENGLISH,
            LocalDate.now().minusDays(10),
            LocalDate.now().plusDays(30),
            166,
            labelT16
        );
        movieDune.setCode("P004");
        movieDune.setCategoryList(new java.util.HashSet<>(Arrays.asList(catSciFi, catAction)));
        movieDune.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmt3D, fmtIMAX2D, fmtIMAX3D)));
        movieDune.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub, transDub)));
        movieDune.setCrewMemberList(new java.util.HashSet<>(Arrays.asList(villeneuve, timothee, zendaya)));
        movieDune = movieRepository8.save(movieDune);

        Movie8 movieOppen = new Movie8(
            null,
            "Oppenheimer",
            "Câu chuyện về nhà vật lý lý thuyết J. Robert Oppenheimer, người đứng đầu Dự án Manhattan nghiên cứu và chế tạo bom nguyên tử.",
            "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=uYPbbksJxIg",
            BigDecimal.valueOf(10000),
            MovieLanguage8.ENGLISH,
            LocalDate.now().minusDays(20),
            LocalDate.now().plusDays(20),
            180,
            labelT18
        );
        movieOppen.setCode("P005");
        movieOppen.setCategoryList(new java.util.HashSet<>(Arrays.asList(catDrama)));
        movieOppen.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmtIMAX2D)));
        movieOppen.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub)));
        movieOppen.setCrewMemberList(new java.util.HashSet<>(Arrays.asList(nolan, cillian)));
        movieOppen = movieRepository8.save(movieOppen);

        Movie8 movieMai = new Movie8(
            null,
            "Mai",
            "Một phụ nữ gần 40 tuổi làm nghề mát-xa luôn đối mặt với định kiến và sự kỳ thị, tìm thấy tình yêu với chàng trai trẻ hơn mình.",
            "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=Zfhpw6Qk3vU",
            BigDecimal.valueOf(0),
            MovieLanguage8.VIETNAMESE,
            LocalDate.now().minusDays(5),
            LocalDate.now().plusDays(40),
            131,
            labelT18
        );
        movieMai.setCode("P006");
        movieMai.setCategoryList(new java.util.HashSet<>(Arrays.asList(catDrama)));
        movieMai.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D)));
        movieMai.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transSub, transVoice)));
        movieMai = movieRepository8.save(movieMai);

        Movie8 moviePanda = new Movie8(
            null,
            "Kung Fu Panda 4",
            "Po được chọn làm Thủ lĩnh tinh thần của Thung lũng Bình Yên và phải tìm người kế vị Thần Long Đại Hiệp mới trong khi đối đầu phản diện Tắc Kè Bông.",
            "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=500&auto=format&fit=crop",
            "https://www.youtube.com/watch?v=_inKs4eeHiI",
            BigDecimal.valueOf(5000),
            MovieLanguage8.ENGLISH,
            LocalDate.now().minusDays(2),
            LocalDate.now().plusDays(25),
            94,
            labelP
        );
        moviePanda.setCode("P007");
        moviePanda.setCategoryList(new java.util.HashSet<>(Arrays.asList(catAnimation, catAction)));
        moviePanda.setFormatList(new java.util.HashSet<>(Arrays.asList(fmt2D, fmt3D)));
        moviePanda.setTransTypeList(new java.util.HashSet<>(Arrays.asList(transDub, transSub)));
        moviePanda = movieRepository8.save(moviePanda);

        LocalDate today = LocalDate.now();
        Showtime8 st1 = new Showtime8(
            null,
            today,
            LocalTime.of(11, 46),
            slot1,
            room3,
            transSub,
            fmtIMAX2D,
            movieDune
        );
        showtimeRepository8.save(st1);

        Showtime8 st2 = new Showtime8(
            null,
            today,
            LocalTime.of(13, 11),
            slot2,
            room1,
            transDub,
            fmt2D,
            moviePanda
        );
        showtimeRepository8.save(st2);

        Showtime8 st3 = new Showtime8(
            null,
            today.plusDays(1),
            LocalTime.of(20, 15),
            slot4,
            room2,
            transSub,
            fmt2D,
            movieOppen
        );
        showtimeRepository8.save(st3);

        System.out.println(">>> Sample Cinema Showtime data initialized successfully with Repositories8! <<<");
    }
}
