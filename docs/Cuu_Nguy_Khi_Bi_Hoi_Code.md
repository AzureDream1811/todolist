# 🆘 HƯỚNG DẪN CỨU NGUY KHI BỊ HỎI CODE
## Khi không nhớ gì hết mà giáo viên hỏi

---

## 🎯 CHIẾN LƯỢC TỔNG QUÁT

### **Nguyên tắc vàng: "STALL → NAVIGATE → EXPLAIN"**

1. **STALL** (5-10s): Câu giờ để suy nghĩ
2. **NAVIGATE** (10-15s): Mở code tìm đúng chỗ
3. **EXPLAIN** (30s): Giải thích dựa vào code đang nhìn

---

## 📚 PHẦN 1: CÂU "CỨU NGUY" - STALLING TACTICS

### **A. Khi bị hỏi bất ngờ:**

#### ✅ **Tốt - Thể hiện suy nghĩ:**
```
"Dạ, em xin phép suy nghĩ một chút..."
"À, cho em mở code để trình bày cho rõ ạ..."
"Câu hỏi hay ạ, em sẽ giải thích chi tiết..."
"Em sẽ show luôn code để thầy/cô dễ theo dõi ạ..."
```

#### ✅ **Rất tốt - Nhắc lại câu hỏi (gain time):**
```
"Dạ, thầy hỏi là [nhắc lại câu hỏi]... Em hiểu đúng không ạ?"
"Thầy muốn em giải thích về [topic X] đúng không ạ?"
```

#### ❌ **Tránh:**
```
"Em không nhớ ạ"
"Em không biết"
"Ơ... à... ừm..."
*Im lặng quá lâu*
```

---

### **B. Khi thực sự không biết:**

#### ✅ **Chiến lược 1: Thành thật nhưng có giải pháp**
```
"Dạ, phần này em không nhớ rõ lắm, cho em xem lại code 
để giải thích cho chính xác ạ..."
```

#### ✅ **Chiến lược 2: Chuyển hướng sang phần biết**
```
"Dạ, phần này em chưa nắm vững lắm, nhưng em có thể 
giải thích phần [related topic] mà em hiểu rõ hơn ạ..."
```

#### ✅ **Chiến lược 3: Giải thích concept chung trước**
```
"Dạ, về cơ bản thì [giải thích concept tổng quát], 
còn implementation cụ thể em xin phép mở code ạ..."
```

---

## 💻 PHẦN 2: NAVIGATION NHANH - TÌM CODE TRONG 10 GIÂY

### **Phím tắt VS Code quan trọng:**

| Phím tắt | Chức năng | Dùng khi |
|----------|-----------|----------|
| **Ctrl + P** | Quick Open file | Biết tên file |
| **Ctrl + Shift + F** | Search toàn project | Tìm từ khóa |
| **Ctrl + T** | Go to Symbol | Tìm class/method |
| **Ctrl + G** | Go to Line | Biết số dòng |
| **F12** | Go to Definition | Từ usage → definition |
| **Ctrl + Click** | Follow link | Click vào method name |

---

### **ROADMAP TÌM CODE THEO CÂU HỎI:**

#### 🔵 **Q: "Giải thích cách em thêm task?"**

**→ Navigate:**
```
1. Ctrl + P → gõ "TaskServlet"
2. Ctrl + F → tìm "handleAddTask"
3. Đọc nhanh method này (10s)
```

**→ Explain (nhìn vào code):**
```
"Dạ thưa thầy, khi user submit form thêm task:
1. (trỏ dòng getParameter) Em lấy các parameter từ request
2. (trỏ Task task = new Task()) Em tạo object Task
3. (trỏ setTitle, setPriority...) Em set các thuộc tính
4. (trỏ taskDAO.createTask) Em gọi DAO để insert vào database
5. (trỏ sendRedirect) Sau đó redirect về inbox"
```

---

#### 🔵 **Q: "DAO Pattern là gì? Code của em ở đâu?"**

**→ Navigate:**
```
1. Ctrl + P → "TaskDAO.java"
2. Nhìn interface methods
3. Ctrl + P → "TaskDAOImpl.java"
4. Chọn 1 method ví dụ: createTask()
```

**→ Explain:**
```
"Dạ, em có interface TaskDAO (trỏ vào interface)
định nghĩa các phương thức như createTask, getTaskById...

Sau đó em có class TaskDAOImpl (trỏ vào class) implement 
interface này. Ở đây em xử lý kết nối database và các 
PreparedStatement (trỏ vào code PreparedStatement).

Lợi ích là tách biệt logic database khỏi Controller ạ."
```

---

#### 🔵 **Q: "PreparedStatement là gì? Tại sao dùng?"**

**→ Navigate:**
```
Ctrl + Shift + F → search "PreparedStatement"
→ Nhảy đến bất kỳ DAO method nào
```

**→ Explain (đọc code):**
```
"Dạ, em dùng PreparedStatement thay vì Statement thường.
(Trỏ vào dòng: pstmt = conn.prepareStatement(sql))

Ở đây em có SQL với dấu ? là placeholder
(Trỏ: "INSERT INTO tasks ... VALUES (?, ?, ?)")

Sau đó em set giá trị bằng setString, setInt...
(Trỏ: pstmt.setString(1, task.getTitle()))

Lợi ích là chống SQL Injection vì input được escape tự động ạ."
```

---

#### 🔵 **Q: "Session hoạt động như thế nào?"**

**→ Navigate:**
```
Ctrl + P → "AuthServlet" OR "WebUtils"
Ctrl + F → tìm "session"
```

**→ Explain:**
```
"Dạ, khi user login thành công (trỏ AuthServlet.loginHandler):
1. Em tạo session: HttpSession session = request.getSession()
2. Em lưu user vào session: session.setAttribute("currentUser", user)
3. Mỗi request sau, em check session trong WebUtils (mở WebUtils)
4. Nếu không có session → redirect về login"
```

---

#### 🔵 **Q: "Email gửi như thế nào?"**

**→ Navigate:**
```
Ctrl + P → "EmailUtils.java"
```

**→ Explain:**
```
"Dạ em dùng JavaMail API (trỏ import javax.mail)

Em config SMTP Gmail (trỏ vào Properties):
- Host: smtp.gmail.com
- Port: 587
- Enable TLS

Tạo Session với Authenticator (trỏ đoạn này)
Sau đó tạo MimeMessage, set recipient, subject, body
Cuối cùng Transport.send() (trỏ vào)

Em gửi async bằng Thread mới để không block (trỏ sendEmailAsync)"
```

---

#### 🔵 **Q: "MVC trong project của em thế nào?"**

**→ Navigate:**
```
Mở 3 files song song:
1. AppServlet.java (Controller)
2. TaskDAO.java (Model)
3. Inbox.jsp (View)
```

**→ Explain:**
```
"Dạ em áp dụng MVC:

Model (trỏ TaskDAO, Task.java): Xử lý data và database
View (trỏ JSP): Hiển thị giao diện, em dùng JSTL
Controller (trỏ Servlet): Nhận request, gọi Model, forward View

Flow: Request → Servlet → DAO → DB
                  ↓
              JSP (forward)
```

---

## 🧠 PHẦN 3: PATTERN CODE CẦN NHỚ (Học thuộc 5 đoạn này)

### **1. Kết nối Database (DAOFactory)**
```java
// Học thuộc 5 dòng này:
Properties props = new Properties();
props.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
String url = props.getProperty("db.url");
Connection conn = DriverManager.getConnection(url, user, password);
return conn;
```

### **2. PreparedStatement cơ bản**
```java
// Pattern chung:
String sql = "INSERT INTO tasks (title, user_id) VALUES (?, ?)";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, title);
pstmt.setLong(2, userId);
pstmt.executeUpdate();
```

### **3. Session Management**
```java
// Check login:
HttpSession session = request.getSession(false);
if (session == null || session.getAttribute("currentUser") == null) {
    response.sendRedirect("/auth/login");
    return null;
}
return (User) session.getAttribute("currentUser");
```

### **4. Forward vs Redirect**
```java
// Forward (cùng request):
request.getRequestDispatcher("/WEB-INF/views/app/Inbox.jsp").forward(request, response);

// Redirect (request mới):
response.sendRedirect(request.getContextPath() + "/app/inbox");
```

### **5. JSTL trong JSP**
```jsp
<!-- Học thuộc cú pháp: -->
<c:forEach var="task" items="${tasks}">
    <li>${task.title}</li>
</c:forEach>

<c:if test="${not empty error}">
    <p>${error}</p>
</c:if>
```

---

## 🎭 PHẦN 4: ROLE-PLAY - TÌNH HUỐNG THỰC TẾ

### **Tình huống 1: Quên hết flow thêm task**

**Giáo viên:** "Em giải thích cách thêm task?"

**❌ Sai:**
> "Dạ... em không nhớ lắm ạ..."

**✅ Đúng:**
> "Dạ, cho em mở code TaskServlet để giải thích cho rõ ạ...  
> (Mở TaskServlet, tìm handleAddTask)  
> Ok, khi user click Add Task thì:  
> 1. Đầu tiên em lấy các tham số từ form (trỏ getParameter)  
> 2. Tạo Task object và set thuộc tính (trỏ code)  
> 3. Gọi DAO để insert (trỏ taskDAO.createTask)  
> 4. Redirect về inbox (trỏ sendRedirect)"

---

### **Tình huống 2: Không biết khái niệm**

**Giáo viên:** "Dependency Injection là gì?"

**❌ Sai:**
> "Dạ em không biết ạ"

**✅ Đúng (thành thật + chuyển hướng):**
> "Dạ em chưa học sâu về Dependency Injection, nhưng trong  
> project em có áp dụng Dependency qua DAOFactory (mở code).  
> Factory này inject các DAO instance vào Servlet ạ..."

---

### **Tình huống 3: Hỏi chi tiết kỹ thuật không nhớ**

**Giáo viên:** "Connection Pool em dùng thế nào?"

**✅ Đúng (thành thật + concept):**
> "Dạ, project này em chưa implement Connection Pool vì scope  
> nhỏ và demo. Hiện tại mỗi request tạo connection mới (mở code).  
> Nếu scale up, em biết cần dùng HikariCP hoặc Apache DBCP để  
> quản lý pool connection cho hiệu quả hơn ạ."

---

## 📌 PHẦN 5: CHECKLIST "KHI HOẢNG LOẠN"

### **Step-by-step xử lý khi bí:**

```
□ STEP 1: Thở sâu 2 giây - đừng hoảng
□ STEP 2: Nói: "Dạ cho em mở code ạ..."
□ STEP 3: Ctrl + P → tìm file liên quan
□ STEP 4: Đọc lướt code 5 giây
□ STEP 5: Giải thích theo code đang nhìn (KHÔNG CẦN NHỚ)
□ STEP 6: Trỏ chuột vào từng dòng code khi giải thích
```

---

## 💡 TÂM LÝ HỌC - HIỂU GIÁO VIÊN

### **Giáo viên KHÔNG mong đợi:**
- ❌ Bạn nhớ 100% code
- ❌ Giải thích như robot
- ❌ Không được xem code

### **Giáo viên MONG MUỐN:**
- ✅ Bạn hiểu concept
- ✅ Có thể navigate và đọc code
- ✅ Giải thích được logic
- ✅ Thể hiện tư duy

### **→ Kết luận:**
**MỞ CODE RA VÀ GIẢI THÍCH LÀ HOÀN TOÀN BÌN̓H THƯỜNG!**

---

## 🔥 CÔNG THỨC BẤT BẠI

### **Template trả lời mọi câu hỏi code:**

```
"Dạ [nhắc lại câu hỏi], cho em mở code [tên file] ạ...

(Mở code, tìm đúng chỗ)

Ok thưa thầy/cô, em giải thích luôn trên code:

1. [Trỏ vào phần đầu] Đầu tiên em [action 1]
2. [Trỏ tiếp] Sau đó em [action 2]
3. [Trỏ tiếp] Cuối cùng em [action 3]

Mục đích là để [giải thích WHY] ạ."
```

### **Ví dụ áp dụng:**

**Q: "Session của em hoạt động thế nào?"**

```
"Dạ về session management, cho em mở WebUtils ạ...

(Ctrl + P → WebUtils, tìm validateAndGetUser)

Ok thầy, em giải thích trên code:

1. (Trỏ dòng getSession(false)) Đầu tiên em lấy session, 
   false nghĩa là không tạo mới nếu chưa có
   
2. (Trỏ if statement) Nếu session null hoặc không có currentUser,
   em redirect về login
   
3. (Trỏ return) Nếu có, em return User object

Mục đích là để check authentication cho mọi trang yêu cầu login ạ."
```

---

## 🎯 TÓM TẮT: 5 ĐIỀU QUAN TRỌNG NHẤT

### **1. ĐỪNG SỢ MỞ CODE**
- Giáo viên mong muốn bạn show code
- Mở code = thể hiện bạn quen thuộc với project

### **2. TRỎ CHUỘT KHI NÓI**
- Trỏ vào từng dòng code
- Giúp giáo viên follow
- Giúp BẠN nhớ phải nói gì tiếp

### **3. NÓI CHẬM, RÕ**
- Đừng nói nhanh vì hồi hộp
- Pause giữa các câu
- Câu giờ để suy nghĩ

### **4. THÀNH THẬT NẾU KHÔNG BIẾT**
- "Em chưa nắm rõ phần này nhưng..."
- "Em biết concept là... còn chi tiết..."
- Chuyển sang phần mình biết

### **5. TỰ TIN**
- Bạn đã code project này
- Bạn chỉ cần nhớ lại
- Code đang nằm đó, mở ra là có

---

## 📖 PHẦN PHỤ LỤC: MAP FILE - BIẾT TÌM GÌ Ở ĐÂU

### **Câu hỏi → File cần mở:**

| Câu hỏi về | File cần mở | Method/Class |
|-----------|-------------|--------------|
| Login/Register | AuthServlet.java | loginHandler, registerHandler |
| Session | WebUtils.java | validateAndGetUser |
| Thêm/Sửa/Xóa Task | TaskServlet.java | handleAddTask, updateTask, deleteTask |
| Database connection | DAOFactory.java | getConnection |
| PreparedStatement | TaskDAOImpl.java | createTask, updateTask |
| Email | EmailUtils.java | sendEmail, sendEmailAsync |
| Upload file | ProfileServlet.java | handleAvatarUpload |
| Admin role | WebUtils.java | validateAdminAndGetUser |
| MVC | AppServlet + TaskDAO + Inbox.jsp | showInbox |
| JSTL | Bất kỳ JSP nào | <c:forEach>, <c:if> |

---

## 🆘 CÂU KHẨN CẤP CUỐI CÙNG

**Nếu thực sự bí toàn tập:**

> "Dạ thưa thầy/cô, em xin thành thật là phần này em chưa  
> nắm chắc lắm. Nhưng em có thể giải thích concept tổng quát  
> là [nói khái niệm chung]. Em sẽ học thêm để hiểu sâu hơn ạ."

**→ Giáo viên sẽ đánh giá cao sự thành thật + thái độ học hỏi**

---

## ✅ KẾT LUẬN

### **Remember:**
- 🧠 Bạn không cần nhớ code
- 💻 Bạn cần biết TÌM và ĐỌC code
- 🗣️ Bạn cần GIẢI THÍCH được logic
- 😊 Bạn cần TỰ TIN

### **Công thức thành công:**
```
Tự tin + Mở code + Trỏ chuột + Giải thích logic = ✅ ĐẬU
```

---

**Good luck! Bạn làm được! 💪**

*"Code is there, you just need to find and explain it!"*
