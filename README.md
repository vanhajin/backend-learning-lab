# Backend Learning Lab

Website học Java/Spring Boot, Claude và AI hỗ trợ phát triển phần mềm theo đặc tả. Frontend và backend được tách riêng; tiến độ học được lưu theo tài khoản.

## Chạy backend

```powershell
cd backend
mvn spring-boot:run
```

Backend chạy tại `http://localhost:8080` và dùng PostgreSQL. Tạo tài khoản bằng địa chỉ `@gmail.com` cùng mật khẩu riêng (tối thiểu 8 ký tự). Mật khẩu được hash bằng BCrypt; tiến độ lưu theo tài khoản trong PostgreSQL.

Khi chạy local, cho backend biết origin của frontend và có thể tắt thuộc tính Secure của cookie vì đang dùng HTTP:

```text
FRONTEND_URL=http://localhost:5173
SESSION_COOKIE_SECURE=false
SESSION_COOKIE_SAME_SITE=lax
```

Trên Render, giữ `SESSION_COOKIE_SECURE=true`, dùng `SameSite=None` cho cookie và đặt `FRONTEND_URL` bằng domain frontend. Frontend và backend đang chạy ở hai domain Render riêng, nên cookie phiên và CSRF cần được gửi qua origin khác. Email xác minh và chức năng đặt lại mật khẩu chưa được cấu hình.

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

Mở `http://localhost:5173`, đăng ký/đăng nhập rồi chọn khóa học. Tiến độ cũ trên trình duyệt được nhập vào tài khoản lần đầu nếu tài khoản chưa có tiến độ.
