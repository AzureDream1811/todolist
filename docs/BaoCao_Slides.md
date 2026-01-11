# 📋 BÁO CÁO ĐỒ ÁN
## Ứng dụng Quản Lý Công Việc - TodoList

---

## 📌 Slide 1: Giới thiệu đồ án

### **Tên đồ án:** TodoList - Ứng dụng Quản Lý Công Việc

### **Mô tả:**
- Ứng dụng web quản lý công việc cá nhân (To-Do List)
- Hỗ trợ người dùng tổ chức, theo dõi và hoàn thành các task hiệu quả
- Phân loại công việc theo dự án (Project)
- Hệ thống nhắc nhở qua email

### **Thành viên nhóm:**
| STT | Họ và Tên | MSSV | Vai trò |
|-----|-----------|------|---------|
| 1   | [Tên TV1] | [MSSV] | Trưởng nhóm / Backend |
| 2   | [Tên TV2] | [MSSV] | Frontend / UI-UX |
| 3   | [Tên TV3] | [MSSV] | Database / Testing |

> *(Cập nhật thông tin thành viên của bạn)*

---

## 📌 Slide 2: Các tính năng cơ bản

### **🔐 1. Quản lý tài khoản (Authentication)**
- Đăng ký tài khoản mới (với xác thực email)
- Đăng nhập / Đăng xuất
- Phân quyền: **User** và **Admin**
- Quản lý profile (cập nhật avatar)

### **📝 2. Quản lý Task (Công việc)**
- Thêm mới task với các thuộc tính:
  - Tiêu đề, Mô tả
  - Độ ưu tiên (Priority: 1-3)
  - Ngày hết hạn (Due Date)
  - Gắn vào Project
- Sửa / Xóa task
- Đánh dấu hoàn thành task
- Tìm kiếm task

### **📂 3. Quản lý Project (Dự án)**
- Tạo mới / Sửa / Xóa project
- Xem danh sách task theo project
- Phân loại công việc theo dự án

### **📅 4. Phân loại & Lọc Task**
- **Inbox**: Tất cả task
- **Today**: Task hôm nay + quá hạn
- **Upcoming**: Task sắp tới
- **Completed**: Task đã hoàn thành

### **📧 5. Hệ thống thông báo Email**
- Email chào mừng khi đăng ký
- Thông báo khi tạo task mới
- Nhắc nhở task quá hạn / hôm nay / ngày mai khi đăng nhập

### **👨‍💼 6. Trang Admin**
- Dashboard thống kê tổng quan
- Quản lý Users (promote/demote/delete)
- Quản lý Tasks (view/delete)
- Quản lý Projects (view/delete)

---

## 📌 Slide 3: Các kỹ thuật web sử dụng

### **🛠 Backend**
| Công nghệ | Mô tả |
|-----------|-------|
| **Java 21** | Ngôn ngữ lập trình chính |
| **Java Servlet 4.0** | Xử lý HTTP Request/Response |
| **JSP (JavaServer Pages)** | Template engine cho View |
| **JSTL 1.2** | Thư viện thẻ tiêu chuẩn JSP |
| **DAO Pattern** | Tách biệt logic truy cập dữ liệu |
| **Factory Pattern** | Quản lý các DAO instances |

### **🗄 Database**
| Công nghệ | Mô tả |
|-----------|-------|
| **MySQL 8.0** | Hệ quản trị CSDL |
| **MySQL Connector/J 9.3** | JDBC Driver kết nối Java-MySQL |
| **Prepared Statement** | Chống SQL Injection |

### **🎨 Frontend**
| Công nghệ | Mô tả |
|-----------|-------|
| **HTML5 / CSS3** | Cấu trúc và giao diện |
| **JavaScript** | Xử lý tương tác client-side |
| **Responsive Design** | Tương thích đa thiết bị |

### **📧 Email Service**
| Công nghệ | Mô tả |
|-----------|-------|
| **JavaMail API 1.6.2** | Gửi email SMTP |
| **Gmail SMTP** | Server gửi mail |
| **Async Threading** | Gửi email không đồng bộ |

### **🔧 Build & Deploy**
| Công nghệ | Mô tả |
|-----------|-------|
| **Maven 3.6+** | Quản lý dependencies & build |
| **Apache Tomcat 9/10** | Servlet Container |
| **WAR Packaging** | Đóng gói ứng dụng |

### **🏗 Kiến trúc MVC**
```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐
│   Browser   │────▶│   Servlet    │────▶│    DAO      │
│   (View)    │◀────│ (Controller) │◀────│   (Model)   │
└─────────────┘     └──────────────┘     └─────────────┘
      ▲                    │                    │
      │                    ▼                    ▼
      │              ┌──────────┐        ┌──────────┐
      └──────────────│   JSP    │        │  MySQL   │
                     └──────────┘        └──────────┘
```

---

## 📌 Slide 4: Cấu trúc thư mục dự án

```
todolist/
├── pom.xml                          # Maven configuration
├── src/main/
│   ├── java/web/
│   │   ├── controller/              # Servlets (Controller)
│   │   │   ├── AdminServlet.java
│   │   │   ├── AppServlet.java
│   │   │   ├── AuthServlet.java
│   │   │   ├── ProfileServlet.java
│   │   │   ├── ProjectServlet.java
│   │   │   └── TaskServlet.java
│   │   ├── dao/                     # Data Access Objects
│   │   │   ├── DAOFactory.java
│   │   │   ├── UserDAO.java
│   │   │   ├── TaskDAO.java
│   │   │   └── ProjectDAO.java
│   │   ├── model/                   # Entity classes
│   │   │   ├── User.java
│   │   │   ├── Task.java
│   │   │   └── Project.java
│   │   └── utils/                   # Helper classes
│   │       ├── EmailUtils.java
│   │       ├── ValidationUtils.java
│   │       └── WebUtils.java
│   ├── resources/
│   │   ├── database.properties
│   │   └── database/*.sql
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── web.xml
│       │   └── views/               # JSP pages
│       │       ├── admin/
│       │       ├── app/
│       │       ├── auth/
│       │       └── component/
│       └── static/css/              # Stylesheets
```

---

## 📌 Slide 5: Database Schema

### **Sơ đồ ERD**
```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    USERS     │       │   PROJECTS   │       │    TASKS     │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │       │ id (PK)      │       │ id (PK)      │
│ username     │◀──┐   │ name         │◀──┐   │ title        │
│ password     │   │   │ user_id (FK) │───┘   │ description  │
│ email        │   │   │ created_at   │       │ priority     │
│ avatar       │   │   └──────────────┘       │ due_date     │
│ role         │   │                          │ completed_at │
│ created_at   │   └──────────────────────────│ user_id (FK) │
└──────────────┘                              │ project_id(FK)│
                                              │ created_at   │
                                              └──────────────┘
```

### **Quan hệ:**
- **Users** (1) ──── (N) **Projects**: Một user có nhiều project
- **Users** (1) ──── (N) **Tasks**: Một user có nhiều task
- **Projects** (1) ──── (N) **Tasks**: Một project có nhiều task

---

## 📌 Slide 6: Phân công công việc nhóm

| Thành viên | Nhiệm vụ | Hoàn thành |
|------------|----------|------------|
| **[Tên TV1]** | - Thiết kế database schema | ✅ |
|               | - Xây dựng DAO layer | ✅ |
|               | - Backend AuthServlet, UserDAO | ✅ |
| **[Tên TV2]** | - Frontend UI/UX design | ✅ |
|               | - Các trang JSP (Login, Register, Inbox) | ✅ |
|               | - CSS styling toàn ứng dụng | ✅ |
| **[Tên TV3]** | - TaskServlet, ProjectServlet | ✅ |
|               | - Email notification system | ✅ |
|               | - Testing & Bug fixing | ✅ |
| **Cả nhóm**   | - Admin module | ✅ |
|               | - Documentation | ✅ |
|               | - Deployment | ✅ |

> *(Cập nhật thông tin phân công thực tế của nhóm)*

---

## 📌 Slide 7: Demo giao diện

### **1. Trang đăng nhập / Đăng ký**
- Form đăng nhập với validation
- Form đăng ký với kiểm tra email, password

### **2. Trang chính (Inbox)**
- Sidebar navigation
- Danh sách task với filter
- Form thêm task nhanh

### **3. Quản lý Task**
- Thêm/Sửa/Xóa task
- Đánh dấu hoàn thành
- Filter theo ngày/project

### **4. Quản lý Project**
- Tạo project mới
- Xem task theo project

### **5. Admin Dashboard**
- Thống kê tổng quan
- Quản lý users/tasks/projects

> *(Chèn screenshots vào slides PowerPoint)*

---

## 📌 Slide 8: Kết quả đạt được

### **✅ Hoàn thành:**
1. **Hệ thống xác thực** hoàn chỉnh (Login/Register/Logout)
2. **CRUD đầy đủ** cho Task và Project
3. **Phân quyền** User/Admin rõ ràng
4. **Hệ thống email** thông báo và nhắc nhở
5. **Giao diện** responsive, thân thiện
6. **Admin panel** quản lý toàn bộ hệ thống
7. **Search functionality** tìm kiếm task
8. **Upload avatar** cho profile người dùng

### **📊 Thống kê code:**
| Thành phần | Số file | Lines of Code (ước tính) |
|------------|---------|--------------------------|
| Servlets (Controller) | 6 | ~1,500 |
| DAO (Model) | 7 | ~800 |
| JSP (View) | 15+ | ~2,000 |
| CSS | 15+ | ~1,500 |
| SQL | 4 | ~100 |
| **Tổng** | **45+** | **~6,000** |

### **🎯 Kỹ năng đạt được:**
- Hiểu và áp dụng mô hình **MVC**
- Sử dụng **Java Servlet** xử lý HTTP
- Thiết kế **database** và viết **SQL**
- Áp dụng **Design Pattern** (DAO, Factory)
- Xây dựng hệ thống **xác thực & phân quyền**
- Tích hợp **email service** với JavaMail
- **Responsive design** với CSS

---

## 📌 Slide 9: Hướng phát triển

### **🔮 Cải tiến trong tương lai:**
1. **Password hashing** với BCrypt/Argon2
2. **JWT authentication** thay thế session
3. **RESTful API** cho mobile app
4. **Caching** với Redis
5. **Task scheduling** (cron job nhắc nhở)
6. **Collaboration** - chia sẻ project với team
7. **Dark mode** theme
8. **Export** task ra PDF/Excel

---

## 📌 Slide 10: Q&A

### **Cảm ơn quý thầy/cô và các bạn đã lắng nghe!**

**Thông tin liên hệ:**
- GitHub: [Link repository]
- Email: [Email nhóm]

---

## 📎 Phụ lục: Hướng dẫn chạy dự án

### **1. Cài đặt Database:**
```bash
mysql -u root -p < src/main/resources/database/01_admin_setup.sql.sql
mysql -u root -p < src/main/resources/database/02_schema.sql.sql
mysql -u root -p < src/main/resources/database/03_sample_data.sql
```

### **2. Build project:**
```bash
mvn clean package
```

### **3. Deploy:**
- Copy `target/todolist.war` vào `Tomcat/webapps/`
- Truy cập: `http://localhost:8080/todolist/`

### **4. Tài khoản mặc định:**
- **Admin**: admin / admin
- **User**: (đăng ký mới)

---

*Báo cáo được tạo cho môn Lập trình Web - Học kỳ 1/2025*
