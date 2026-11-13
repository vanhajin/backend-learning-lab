# Backend Learning Lab

Website học Java/Spring Boot, tách frontend và backend.

## Chạy backend

```powershell
cd backend
mvn spring-boot:run
```

Backend chạy tại `http://localhost:8080` và dùng PostgreSQL. Khi deploy, cấu hình ba biến môi trường: `DATABASE_JDBC_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`.

Ví dụ local với PostgreSQL:

```text
DATABASE_JDBC_URL=jdbc:postgresql://localhost:5432/backend_lab
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-password
```

## Chạy frontend

```powershell
cd frontend
npm install
npm run dev
```

Mở `http://localhost:5173`. Bản frontend hiện dùng nội dung học local; API backend đã sẵn sàng cho việc đồng bộ tiến độ ở bước tiếp theo.
