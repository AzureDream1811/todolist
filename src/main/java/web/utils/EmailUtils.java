package web.utils;

import web.model.Task;
import web.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class EmailUtils {
    private static final Properties dbProps = new Properties();
    static {
        try {
            // Load file properties từ resources
            dbProps.load(EmailUtils.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Thay bằng Email và App Password của bạn
    private static final String FROM_EMAIL = dbProps.getProperty("mail.smtp.user");
    private static final String APP_PASSWORD = dbProps.getProperty("mail.smtp.app.password");

    /**
     * Gửi email ở chế độ bất đồng bộ (Async) để không làm treo giao diện
     */
    public static void sendEmailAsync(String toEmail, String subject, String body) {
        new Thread(() -> {
            sendEmail(toEmail, subject, body);
        }).start();
    }

    /**
     * Hàm xử lý gửi email chính dùng t hư viện javax.mail
     */
    public static void sendEmail(String toEmail, String subject, String body) {
        // 1. Cấu hình Server SMTP của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        // 2. Xác thực tài khoản
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            // 3. Tạo nội dung thư
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");

            // 2. Đảm bảo nội dung HTML sử dụng charset UTF-8
            message.setContent(body, "text/html; charset=UTF-8");

            // 4. Thực thi gửi
            Transport.send(message);
            System.out.println("==> Đã gửi email thành công đến: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("==> Lỗi gửi Email: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // Thêm vào EmailUtils.java
    public static void sendTaskReminder(User user, List<Task> overdue, List<Task> today, List<Task> tomorrow) {
        StringBuilder sb = new StringBuilder();

        // Wrapper chính
        sb.append("<div style='background-color: #f8f9fa; padding: 30px 10px; font-family: Roboto, Arial, sans-serif;'>");

        // Card nội dung
        sb.append("<div style='max-width: 520px; margin: 0 auto; background-color: #ffffff; border: 1px solid #dadce0; border-radius: 8px; overflow: hidden;'>");

        // Thanh màu xanh phía trên
        sb.append("<div style='background-color: #1a73e8; height: 8px;'></div>");

        sb.append("<div style='padding: 24px;'>");

        // Tiêu đề chào (22px)
        sb.append("<h1 style='font-size: 22px; color: #202124; margin: 0 0 16px 0; font-weight: 500;'>Chào ").append(user.getUsername()).append(",</h1>");

        // Lời dẫn (14px - chuẩn GG)
        sb.append("<p style='font-size: 14px; color: #5f6368; line-height: 20px; margin-bottom: 24px;'>");
        sb.append("Hệ thống ghi nhận bạn vừa đăng nhập. Đây là tóm tắt danh sách công việc cần lưu ý để bạn có một ngày làm việc hiệu quả.");
        sb.append("</p>");

        // Render các mục
        if (!overdue.isEmpty()) {
            renderTaskSection(sb, "🔴 Công việc quá hạn", "#d93025", overdue, true);
        }
        if (!today.isEmpty()) {
            renderTaskSection(sb, "📅 Hôm nay (Trước 0h)", "#f29900", today, false);
        }
        if (!tomorrow.isEmpty()) {
            String tomorrowDate = java.time.LocalDate.now().plusDays(1).toString();
            renderTaskSection(sb, "🟢 Ngày mai (" + tomorrowDate + ")", "#1e8e3e", tomorrow, false);
        }

        // Nút Xem chi tiết (Kích thước vừa vặn)
        sb.append("<div style='margin-top: 24px; text-align: center;'>");
        sb.append("<a href='http://localhost:8080/todolist/auth/login' style='background-color: #1a73e8; color: white; padding: 10px 24px; text-decoration: none; border-radius: 4px; font-weight: 500; font-size: 14px; display: inline-block;'>Xem chi tiết</a>");
        sb.append("</div>");

        sb.append("</div>"); // End padding

        // Footer xám nhẹ
        sb.append("<div style='background-color: #f1f3f4; padding: 16px 24px; text-align: center; border-top: 1px solid #dadce0;'>");
        sb.append("<p style='font-size: 12px; color: #70757a; margin: 0;'>Chúc bạn một ngày làm việc hiệu quả!</p>");
        sb.append("</div>");

        sb.append("</div>"); // End Card
        sb.append("</div>"); // End Wrapper

        sendEmailAsync(user.getEmail(), "Thông báo công việc - " + user.getUsername(), sb.toString());
    }

    // Hàm bổ trợ để vẽ từng phần công việc
    private static void renderTaskSection(StringBuilder sb, String title, String color, List<Task> tasks, boolean showDate) {
        sb.append("<div style='margin-bottom: 20px;'>");

        // Tiêu đề mục (16px)
        sb.append("<h2 style='font-size: 16px; color: ").append(color).append("; margin: 0 0 12px 0; border-bottom: 1px solid #eee; padding-bottom: 8px; font-weight: 500;'>").append(title).append("</h2>");

        for (Task t : tasks) {
            // Thanh kẻ dọc mỏng (3px) như trong ảnh mẫu
            sb.append("<div style='margin-bottom: 12px; padding-left: 12px; border-left: 3px solid ").append(color).append(";'>");

            // Tên task (14px - Bold)
            sb.append("<div style='font-size: 14px; font-weight: 600; color: #202124;'>").append(t.getTitle()).append("</div>");

            // Mô tả (13px)
            if (t.getDescription() != null && !t.getDescription().isEmpty()) {
                sb.append("<div style='font-size: 13px; color: #5f6368; margin-top: 2px;'>").append(t.getDescription()).append("</div>");
            }

            // Ngày tháng (12px)
            if (showDate && t.getDueDate() != null) {
                sb.append("<div style='font-size: 12px; color: #d93025; margin-top: 4px;'>Hạn: ").append(t.getDueDate()).append("</div>");
            }
            sb.append("</div>");
        }
        sb.append("</div>");
    }
}