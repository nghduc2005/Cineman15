@echo off
chcp 65001 > nul
title CineMan - PTIT BTL Nhom 8 Launcher

echo ===============================================================
echo     HE THONG QUAN LY RAP CHIEU PHIM (CineMan) - PTTK NHOM 8
echo ===============================================================
echo.

cd /d "%~dp0"

echo [1/3] Khoi dong Backend Spring Boot...
if exist "backend\target\cinema-showtime-0.0.1-SNAPSHOT.jar" (
    start "CineMan Backend (Spring Boot:8080)" cmd /k "cd /d ""%~dp0backend"" && java -jar target\cinema-showtime-0.0.1-SNAPSHOT.jar"
) else (
    start "CineMan Backend (Spring Boot:8080)" cmd /k "cd /d ""%~dp0backend"" && mvn spring-boot:run"
)

echo [2/3] Khoi dong Frontend React (Vite)...
start "CineMan Frontend (React:5173)" cmd /k "cd /d ""%~dp0frontend"" && npm run dev"

echo [3/3] Dang cho he thong san sang va mo trinh duyet...
timeout /t 4 /nobreak > nul
start http://localhost:5173

echo.
echo ===============================================================
echo  Toan bo he thong da duoc khoi dong thanh cong!
echo  - Module Lich chieu:   http://localhost:5173/showtime
echo  - Module Tim kiem phim: http://localhost:5173/search
echo  - Backend REST API:     http://localhost:8080
echo  - Database:             MySQL (rapchieuphim on localhost:3306)
echo ===============================================================
echo.

