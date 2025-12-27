# CV/JD Parser Service (spaCy)

Service Python nhỏ để parse CV và JD bằng hai model đã train (ResumeModel, JdModel) và tính điểm match đơn giản cho recruiter.

## Chuẩn bị
- Model đã train đặt tại `ResumeRankingSystem-main/assets/ResumeModel/output/model-best` và `ResumeRankingSystem-main/assets/JdModel/output/model-best`.
- Docker sẽ mount thư mục `ResumeRankingSystem-main/assets` vào `/app/assets` (read-only).

## Build & Run (docker-compose)
Đã khai báo service `cv-parser` trong `docker-compose.yaml`. Chỉ cần:
```bash
docker compose up -d cv-parser
```
Service lắng nghe port 8000 (đã publish ra ngoài nếu cần).

## API
- `POST /parse/cv` (multipart `file` hoặc form `text`):
  - Trả về `{ rawText, entities }` với entities lấy từ model ResumeModel.
- `POST /parse/jd` (multipart `file` hoặc form `text`):
  - Trả về `{ rawText, entities }` với entities lấy từ model JdModel.
- `POST /rank`:
  - Body JSON: `{ "parsedCv": {...}, "parsedJd": {...}, "weights": {"skills":0.6,"experience":0.3,"title":0.1} }`
  - Trả về điểm `score` cùng breakdown.

## Gợi ý gọi từ BE Java
- Cấu hình endpoint: `http://cv-parser:8000` (cùng network Docker).
- Upload CV/JD lên S3 như hiện tại, tải file, rồi `POST /parse/cv` hoặc `/parse/jd`, nhận JSON, lưu vào DB (`parsed_cvs.parsed_json`, v.v.).
- Khi cần ranking: lấy parsedCv + parsedJd, gọi `/rank` hoặc tự tính.
