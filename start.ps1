# CineMan - PTIT BTL Nhom 8 Launcher
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host "    HE THONG QUAN LY RAP CHIEU PHIM (CineMan) - PTTK NHOM 8" -ForegroundColor Cyan
Write-Host "===============================================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "[1/3] Khoi dong Backend Spring Boot..." -ForegroundColor Green
$jarPath = Join-Path $scriptDir "backend\target\cinema-showtime-0.0.1-SNAPSHOT.jar"
if (Test-Path $jarPath) {
    Start-Process cmd -ArgumentList "/k cd /d `"$scriptDir\backend`" && java -jar target\cinema-showtime-0.0.1-SNAPSHOT.jar"
} else {
    Start-Process cmd -ArgumentList "/k cd /d `"$scriptDir\backend`" && mvn spring-boot:run"
}

Write-Host "[2/3] Khoi dong Frontend React (Vite)..." -ForegroundColor Green
Start-Process cmd -ArgumentList "/k cd /d `"$scriptDir\frontend`" && npm run dev"

Write-Host "[3/3] Dang mo trinh duyet web..." -ForegroundColor Green
Start-Sleep -Seconds 4
Start-Process "http://localhost:5173"

Write-Host ""
Write-Host "===============================================================" -ForegroundColor Yellow
Write-Host " Toan bo he thong da duoc khoi dong thanh cong!" -ForegroundColor Yellow
Write-Host " - Module Lich chieu:   http://localhost:5173/showtime" -ForegroundColor White
Write-Host " - Module Tim kiem phim: http://localhost:5173/search" -ForegroundColor White
Write-Host " - Backend REST API:     http://localhost:8080" -ForegroundColor White
Write-Host " - H2 Console DB:        http://localhost:8080/h2-console" -ForegroundColor White
Write-Host "===============================================================" -ForegroundColor Yellow

