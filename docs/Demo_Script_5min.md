# 🎬 KỊCH BẢN DEMO - TODOLIST APPLICATION
## ⏱️ Thời gian: 5 phút

---

## 📋 CHUẨN BỊ TRƯỚC KHI DEMO (Checklist)

```
✅ Tomcat đã chạy (startup.bat)
✅ Database có sample data
✅ Password đã migrate (tất cả user dùng: password123)
✅ Mở trình duyệt ở trang login: http://localhost:8080/todolist/
✅ Đã có sẵn account test: AzureDream/password123 hoặc admin/password123
✅ Slide PowerPoint đã mở sẵn (nếu trình bày trước)
✅ Đóng các ứng dụng không cần thiết để tránh lag
✅ VS Code mở sẵn project (có thể cần show code)
```

---

## ⏰ TIMELINE CHÍNH XÁC

| Thời gian | Nội dung | Thời lượng |
|-----------|----------|------------|
| 0:00 - 0:30 | Giới thiệu tổng quan | 30s |
| 0:30 - 2:00 | **DEMO CHÍNH: Task CRUD** | 90s |
| 2:00 - 3:00 | Project & Filter Views | 60s |
| 3:00 - 4:00 | Admin Panel | 60s |
| 4:00 - 4:45 | Upload Avatar & Email | 45s |
| 4:45 - 5:00 | Tổng kết | 15s |

---

## 🎤 SCRIPT CHI TIẾT

---

### **[0:00 - 0:30] PHẦN 1: GIỚI THIỆU (30 giây)**

**📢 Nói:**
> "Xin chào thầy/cô và các bạn. Em xin phép demo đồ án **TodoList** - ứng dụng quản lý công việc được xây dựng bằng **Java Servlet, JSP** và **MySQL**."
>
> "Ứng dụng áp dụng mô hình **MVC**, **DAO Pattern**, và có đầy đủ tính năng CRUD, phân quyền User/Admin, gửi email thông báo."

**🖱️ Thao tác:**
- Đang ở trang Login

---

### **[0:30 - 2:00] PHẦN 2: DEMO CHÍNH - TASK CRUD (90 giây)** ⭐

#### **A. Đăng nhập (10s)**

**📢 Nói:**
> "Đầu tiên, em đăng nhập với tài khoản user. **Password được hash bằng BCrypt** trước khi lưu database."

**🖱️ Thao tác:**
1. Nhập username: `AzureDream` (hoặc user có sẵn)
2. Nhập password: `password123`
3. Click **Đăng nhập**
4. → Vào trang **Inbox**

*💡 Nếu login fail: Restart Tomcat, kiểm tra password đã migrate chưa*

---

#### **B. Xem danh sách Task (5s)**

**📢 Nói:**
> "Đây là trang Inbox hiển thị tất cả task của user. Em có thể thấy sidebar bên trái với các mục Today, Upcoming, Completed..."

**🖱️ Thao tác:**
- Trỏ chuột qua các task
- Trỏ sidebar

---

#### **C. THÊM Task mới (25s)** ⭐ *Quan trọng*

**📢 Nói:**
> "Em sẽ demo **thêm task mới**. Click vào nút Add Task..."

**🖱️ Thao tác:**
1. Click nút **"+ Add Task"**
2. Điền form nhanh:
   - **Title**: "Demo task cho giáo viên"
   - **Description**: "Kiểm tra chức năng CRUD"
   - **Priority**: Chọn "1" (High)
   - **Due Date**: Chọn ngày mai
   - **Project**: Chọn "Personal" (nếu có)
3. Click **"Add Task"**
4. → Task xuất hiện trong danh sách

**📢 Nói:**
> "Task vừa được thêm thành công. Dữ liệu được lưu vào database qua **TaskDAO** sử dụng **PreparedStatement** để chống SQL Injection. Em dùng **HikariCP connection pool** để tối ưu performance với concurrent users."

---

#### **D. SỬA Task (20s)** ⭐

**📢 Nói:**
> "Tiếp theo, em sẽ **chỉnh sửa** task này."

**🖱️ Thao tác:**
1. Click vào task vừa tạo (hoặc icon Edit)
2. Thay đổi **Priority** từ 1 → 2
3. Sửa **Due Date** sang ngày khác
4. Click **"Update"**
5. → Task được update

**📢 Nói:**
> "Task đã được cập nhật. Em dùng **UPDATE query** trong TaskDAO."

---

#### **E. ĐÁNH DẤU HOÀN THÀNH (15s)** ⭐

**📢 Nói:**
> "Em sẽ **hoàn thành** task này."

**🖱️ Thao tác:**
1. Click checkbox bên cạnh task (hoặc nút Complete)
2. → Task bị gạch ngang / biến mất khỏi Inbox

**📢 Nói:**
> "Khi complete, em set field `completedAt` = ngày hiện tại trong database."

---

#### **F. XÓA Task (15s)** ⭐

**📢 Nói:**
> "Cuối cùng, em demo **xóa task**."

**🖱️ Thao tác:**
1. Click icon **Delete** (hoặc nút xóa)
2. Confirm xóa (nếu có popup)
3. → Task biến mất

**📢 Nói:**
> "Vậy là em đã demo đầy đủ **CRUD** cho Task: Create, Read, Update, Delete."

---

### **[2:00 - 3:00] PHẦN 3: PROJECT & FILTER VIEWS (60 giây)**

#### **A. Tạo Project (20s)**

**📢 Nói:**
> "Em sẽ tạo một **Project** để nhóm các task lại."

**🖱️ Thao tác:**
1. Click **"+ Add Project"** (ở sidebar hoặc Projects page)
2. Nhập tên: "Đồ án Web"
3. Click **Create**
4. → Project xuất hiện trong sidebar

**📢 Nói:**
> "Project giúp phân loại task theo nhóm công việc. Quan hệ 1-N giữa Project và Task."

---

#### **B. Thêm Task vào Project (15s)**

**🖱️ Thao tác:**
1. Click **Add Task**
2. Chọn **Project**: "Đồ án Web"
3. Điền title nhanh: "Hoàn thiện báo cáo"
4. Click Add
5. Click vào Project "Đồ án Web" ở sidebar
6. → Xem task trong project

**📢 Nói:**
> "Các task được filter theo `project_id` qua SQL query."

---

#### **C. Filter Views (25s)**

**📢 Nói:**
> "Em demo các chế độ xem khác nhau."

**🖱️ Thao tác:**
1. Click **"Today"** ở sidebar
   - **Nói**: "Đây là task hôm nay và task quá hạn, query theo `due_date`"
   
2. Click **"Upcoming"**
   - **Nói**: "Các task sắp tới trong tương lai"
   
3. Click **"Completed"**
   - **Nói**: "Task đã hoàn thành, filter theo `completedAt IS NOT NULL`"

4. Thử **Search** (nếu có thời gian)
   - Gõ từ khóa
   - **Nói**: "Search dùng `LIKE` query"

---

### **[3:00 - 4:00] PHẦN 4: ADMIN PANEL (60 giây)** ⭐

**📢 Nói:**
> "Tiếp theo, em demo trang **Admin** với tài khoản admin."

#### **A. Đăng nhập Admin (10s)**

**🖱️ Thao tác:**
1. Click **Logout**
2. Đăng nhập lại:
   - Username: `admin`
   - Password: `password123`
3. → Tự động redirect đến **/admin/dashboard**

**📢 Nói:**
> "Em check role trong session, nếu là ADMIN thì redirect đến admin panel."

---

#### **B. Dashboard - Thống kê (15s)**

**📢 Nói:**
> "Đây là trang Dashboard với thống kê tổng quan."

**🖱️ Thao tác:**
- Trỏ vào các con số:
  - **Total Users**: X users
  - **Total Tasks**: Y tasks
  - **Total Projects**: Z projects
  - **Completed Tasks**: N tasks

**📢 Nói:**
> "Các số liệu được tính qua aggregate queries."

---

#### **C. Quản lý Users (20s)**

**🖱️ Thao tác:**
1. Click **"Users"** ở sidebar admin
2. → Xem danh sách tất cả users

**📢 Nói:**
> "Admin có thể xem tất cả users, promote lên admin hoặc demote về user."

**🖱️ Thao tác:**
3. Click **"Promote"** cho 1 user
4. → Role đổi từ USER → ADMIN

**📢 Nói:**
> "Em update field `role` trong database."

---

#### **D. Quản lý Tasks/Projects (15s)**

**🖱️ Thao tác:**
1. Click **"Tasks"** ở sidebar admin
2. → Xem tất cả tasks của mọi user

**📢 Nói:**
> "Admin có thể xem và xóa task của bất kỳ user nào. Đây là phân quyền dựa trên role."

**🖱️ Thao tác:**
3. Click **"Projects"**
4. → Xem danh sách projects

---

### **[4:00 - 4:45] PHẦN 5: UPLOAD AVATAR & EMAIL (45 giây)**

#### **A. Upload Avatar (20s)**

**📢 Nói:**
> "Em sẽ demo upload avatar."

**🖱️ Thao tác:**
1. Đăng nhập lại user (hoặc ở profile)
2. Click **Profile** ở header
3. Click **"Upload Avatar"**
4. Chọn file ảnh (< 5MB, .jpg/.png)
5. Click **Upload**
6. → Avatar hiển thị

**📢 Nói:**
> "Em dùng `@MultipartConfig` với `Part` để xử lý file upload, validate extension và size, lưu vào folder `uploads/avatars`."

---

#### **B. Email Notification (25s)**

**📢 Nói:**
> "Về tính năng gửi email, em có 2 loại:"

**🖱️ Thao tác:**
1. (Nếu có email test sẵn) Mở Gmail tab
2. Show email "Welcome" khi đăng ký
3. Show email "Task Reminder" khi login

**📢 Nói:**
> "Khi user đăng ký → gửi email chào mừng.  
> Khi login → kiểm tra task quá hạn/hôm nay → gửi email nhắc nhở.  
> Em dùng **JavaMail API** với Gmail SMTP, gửi **async** bằng Thread mới để không block UI."

**🎯 NẾU KHÔNG CÓ EMAIL:**
> "Do giới hạn thời gian demo, em không show email trực tiếp, nhưng code đã implement đầy đủ trong `EmailUtils.java` với JavaMail API và TLS encryption."

---

### **[4:45 - 5:00] PHẦN 6: TỔNG KẾT (15 giây)**

**📢 Nói:**
> "Vậy là em đã demo các tính năng chính:
> - **CRUD đầy đủ** cho Task và Project
> - **Phân quyền** User/Admin
> - **Filter views** theo ngày
> - **Upload file** và **Email notification**
> - Áp dụng **MVC, DAO Pattern, PreparedStatement**
>
> Em xin cảm ơn thầy/cô và các bạn đã lắng nghe. Em sẵn sàng trả lời câu hỏi!"

**🖱️ Thao tác:**
- Quay lại slide PowerPoint (nếu có)
- Hoặc đứng yên, sẵn sàng Q&A

---

## 🎯 LƯU Ý QUAN TRỌNG

### **Nếu bị thiếu thời gian, ƯU TIÊN:**
1. ✅ **Task CRUD** (phần quan trọng nhất - 90s) - Thể hiện PreparedStatement & BCrypt
2. ✅ **Admin Panel** (thể hiện phân quyền role-based - 60s)
3. ✅ **Giải thích Connection Pooling** (1 câu khi add task: "Em dùng HikariCP pool 10 connections")
4. ⚠️ Skip: Upload Avatar nếu thiếu thời gian
5. ⚠️ Email chỉ nói không cần show thật

### **Nếu thừa thời gian:**
- Mở code để show:
  - `TaskServlet.handleAddTask()`
  - `TaskDAO.createTask()`
  - `EmailUtils.sendEmail()`

---

## 📝 TIPS THÀNH CÔNG

### **1. Tốc độ nói:**
- Nói rõ ràng, vừa phải
- Đừng nói quá nhanh vì hồi hộp

### **2. Thao tác chuột:**
- Di chuyển chuột chậm, rõ ràng
- Pause 1-2 giây sau mỗi action để audience theo kịp

### **3. Khi có lỗi:**
- **ĐỪNG HOẢNG LOẠN**
- **Login không được?** → "Password đã được hash bằng BCrypt, cần dùng: password123"
- **Task không hiện?** → "Do connection pool đang bận, em reload lại" (F5)
- **Lag/Loading lâu?** → "Em vừa fix connection leak bằng try-with-resources, nhưng có thể do database busy"
- Hoặc skip sang phần khác: "Do giới hạn thời gian, em xin phép tiếp tục phần tiếp theo"

### **4. Ngôn ngữ cơ thể:**
- Đứng thẳng, tự tin
- Nhìn vào giáo viên khi nói
- Cười tự nhiên

### **5. Kỹ thuật trong lời nói:**
- Nhấn mạnh từ khóa: **"CRUD"**, **"PreparedStatement"**, **"MVC"**
- Không cần giải thích quá chi tiết, demo cho thấy được tính năng là đủ

---

## 🔄 KẾ HOẠCH B (Backup Plan)

### **Nếu Tomcat không chạy:**
```
1. Mở Terminal
2. Chạy: cd d:\Coding\...\todolist
3. Chạy: mvn clean package
4. Giải thích: "Em đang build lại project..."
5. Deploy WAR file
```

### **Nếu Database lỗi:**
```
1. Check MySQL đang chạy
2. Show code connection trong DAOFactory
3. Giải thích cơ chế kết nối
```

### **Nếu Email không gửi được:**
```
"Do Gmail security, email có thể mất vài phút.
Em đã test thành công trước đó, code ở EmailUtils.java"
→ Show code thay vì show email thật
```

---

## ✅ CHECKLIST CUỐI CÙNG

**Trước khi bắt đầu (5 phút trước):**
```
□ Tomcat running: http://localhost:8080/todolist/
□ Test login thử 1 lần
□ Đóng tất cả tab không cần thiết
□ Zoom trình duyệt 110-125% (để dễ nhìn)
□ Tắt notification, DND mode
□ Chuẩn bị 1 chai nước
□ Thở sâu, tự tin!
```

---

## 🎬 SCRIPT RÚT GỌN (Nếu chỉ có 3 phút)

| Thời gian | Nội dung |
|-----------|----------|
| 0:00 - 0:15 | Giới thiệu nhanh |
| 0:15 - 1:30 | Task CRUD (thêm, sửa, xóa, complete) |
| 1:30 - 2:15 | Admin Panel (login admin, xem dashboard, manage users) |
| 2:15 - 2:45 | Upload Avatar + giải thích Email |
| 2:45 - 3:00 | Tổng kết |

---

## 🌟 KẾT THÚC

**Câu nói cuối:**
> "Em xin chân thành cảm ơn! 🙏"

**Good luck! Bạn làm được! 💪**
