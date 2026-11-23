# Thiết kế khóa học AI hỗ trợ phát triển phần mềm theo đặc tả

## Ý định

Đọc tài liệu *Vibe Coding Architecture at Scale* và bổ sung một khóa học tiếng Việt cho người học từ số 0 về cách dùng AI coding agent có kiểm soát, từ đặc tả yêu cầu đến kiến trúc và kiểm chứng. Người học đã biết lập trình, ưu tiên Java/Spring Boot.

## Phát hiện hiện trạng

- Learning Lab hiện có hai khóa: Backend Java/Spring Boot (74 bài) và Claude và AI Engineering (42 bài trong 7 phần).
- Claude hiện đã bao gồm prompt/context cơ bản, Claude Code, CLAUDE.md, Skills, hooks, API và MCP.
- Khóa hiện tại chưa tạo thành một lộ trình đầy đủ cho spec-driven development, fitness functions, kiểm thử ranh giới kiến trúc, contract/schema drift, CI guardrails và quy trình làm việc an toàn theo vòng lặp.
- Tiến độ frontend dùng chung localStorage và định danh bài học; bài mới phải có ID duy nhất để không chạm tiến độ hai khóa hiện có.

## Mục tiêu và ngoài phạm vi

- Thêm khóa thứ ba, bằng tiếng Việt, có nhóm học phần, mục tiêu dễ hiểu, bài giảng, ví dụ, bài tập và câu hỏi tự giải thích.
- Dùng ví dụ ưu tiên Java/Spring Boot nhưng giải thích thuật ngữ cho người mới.
- Giữ nguyên nội dung và hành vi của khóa Backend và khóa Claude hiện tại.
- Không dịch hoặc sao chép nguyên văn sách; nội dung khóa học là phần diễn giải và bài tập mới dựa trên chủ đề/ý tưởng trong tài liệu.
- Không tự deploy hoặc push thay đổi trong phạm vi thiết kế này.

## Cách tiếp cận

1. Gộp nội dung vào khóa Claude hiện tại: tránh khóa thứ ba nhưng trộn kỹ năng sử dụng Claude với kiến trúc hệ thống ở quy mô lớn.
2. Thêm khóa độc lập về AI-assisted engineering/spec-driven architecture: ranh giới học rõ, tiến độ riêng, không ảnh hưởng hai lộ trình cũ. **Đề xuất:** phù hợp nhất với yêu cầu chi tiết từ số 0 và nội dung PDF chuyên sâu.
3. Chỉ tạo danh sách tham khảo PDF: ít thay đổi nhưng không cung cấp bài giảng tương tác trong Learning Lab.

## Cấu trúc dự kiến

Khóa thứ ba “AI hỗ trợ phát triển phần mềm theo đặc tả” chia 8 phần: (1) tư duy nền và giới hạn AI, (2) yêu cầu và đặc tả, (3) context và chỉ dẫn dự án, (4) chia module và ranh giới kiến trúc, (5) kiểm thử hành vi/kiến trúc và fitness functions, (6) vòng lặp spec → code → verify → review, (7) API/schema/CI và bảo mật, (8) dự án Spring Boot tổng kết. Quyết định cuối: 40 bài, mỗi bài có một mục tiêu cụ thể, giải thích, ví dụ, bài tập và câu hỏi tự diễn đạt.

## Quyết định triển khai còn lại

Người dùng đồng ý triển khai thành khóa thứ ba và chọn cách cân bằng thực hành Claude Code với nguyên tắc kiến trúc. Nội dung tiếng Việt là diễn giải giáo dục từ chủ đề sách, không dịch toàn văn. Người dùng cũng xác nhận tài khoản dùng Gmail làm địa chỉ đăng nhập nhưng tự đặt mật khẩu riêng, và duyệt thiết kế session + BCrypt.

## Bổ sung yêu cầu: tài khoản và đồng bộ tiến độ

Người dùng muốn đăng ký/đăng nhập bằng địa chỉ Gmail và mật khẩu riêng (không dùng Google OAuth). Đăng nhập phải gắn tiến độ học với tài khoản để dùng được trên nhiều thiết bị. Backend hiện có PostgreSQL và bảng lesson_progress với learner_id, nhưng API hiện nhận learnerId từ URL và không có xác thực; không thể xem đó là ranh giới tin cậy.

### Đề xuất thiết kế auth

- Đăng ký email/password, xác thực địa chỉ email hợp lệ, hash mật khẩu BCrypt, unique email; không lưu password thuần.
- Dùng Spring Security session cookie HttpOnly/Secure và CSRF token, phù hợp frontend static và API Spring Boot tách origin; không lưu credential/token trong localStorage.
- Chỉ lấy user ID từ session phía server; loại bỏ quyền truy cập tiến độ bằng learnerId tùy ý.
- Tiến độ PostgreSQL gắn với user ID. Khi người dùng đăng nhập/đăng ký lần đầu, có thể nhập phần tiến độ localStorage hiện có nếu chưa có tiến độ trên tài khoản.
- Không thêm Google OAuth hoặc email verification/reset flow ở giai đoạn đầu; đó là phần mở rộng riêng cần nhà cung cấp email/configuration.

### Phương án đã cân nhắc

1. Email/password với session bảo vệ bằng Spring Security, BCrypt, CSRF, PostgreSQL — **đề xuất**, ít thành phần hơn và hợp với cùng backend hiện có.
2. Tích hợp Google OAuth — không phù hợp vì người dùng cần mật khẩu riêng.
3. Tự tạo JWT lưu ở trình duyệt — không đề xuất vì tăng rủi ro bảo vệ token và vận hành refresh/revocation.

Màn hình gồm đăng nhập, chuyển sang đăng ký, email Gmail, mật khẩu và thông báo lỗi/thành công bằng tiếng Việt. Nội dung và tiến độ của Backend, Claude, khóa kiến trúc sẽ cùng được lưu theo account, còn guest/local progress chỉ được hợp nhất lần đầu theo quy tắc đã nêu.
