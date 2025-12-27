# Chạy ứng dụng với Docker

## Yêu cầu
- Docker / Docker Compose
- Đã có file môi trường `envs/.env` (copy từ `envs/.env.template` và điền giá trị).

## Cấu trúc liên quan
- `Dockerfile`: image cho app Spring Boot.
- `docker-compose.yaml`: đã gồm MySQL, Redis và service app.
- `envs/.env`: biến môi trường cho ứng dụng (DB, Redis, JWT, email, Cloudinary...).

## Quy trình chạy
1. Build ứng dụng (tạo jar):
   ```bash
   ./mvnw clean package -DskipTests
   ```
2. Build image và chạy bằng Compose:
   ```bash
   docker compose build
   docker compose up -d
   ```
   - App chạy trên `http://localhost:8080`.
   - MySQL lắng nghe `3307` (theo compose) và Redis trên `6379`.

## Biến môi trường tối thiểu (trong `envs/.env`)
```
DB_HOST=mysql
DB_PORT=3306
DB_NAME=job_hub
DB_USERNAME=jobhub
DB_PASSWORD=123ABC@

REDIS_HOST=redis
REDIS_PORT=6379

JWT_SIGNER_KEY=your-jwt-secret
```
Điền thêm các biến mail, Cloudinary nếu cần.

## Lệnh hữu ích
- Xem log app: `docker compose logs -f app`
- Dừng toàn bộ: `docker compose down`
- Xoá dữ liệu MySQL: `docker volume rm datn_mysql_data` (cẩn thận, mất dữ liệu)

## Ghi chú
- Nếu thay đổi code, rebuild: `./mvnw clean package -DskipTests && docker compose build && docker compose up -d`.
- Các API đang sử dụng prefix `/api/...`; mở CORS trong `SecurityConfig` theo biến môi trường nếu cần thêm origin.
## Nhanh
- Copy envs/.env.template thành envs/.env và điền biến môi trường.
- ./mvnw clean package -DskipTests
- docker compose build && docker compose up -d