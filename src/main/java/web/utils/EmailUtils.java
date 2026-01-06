package web.utils;

import web.model.Task;
import web.model.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Utility class for handling email operations within the application.
 * Provides functionality to send general emails and specific task reminders
 * both synchronously and asynchronously using SMTP.
 */
public class EmailUtils {
    private static final Properties dbProps = new Properties();
    static {
        try {
            dbProps.load(EmailUtils.class.getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException | NullPointerException e) {
            System.err.println("==> Critical: Could not load database.properties.");
        }
    }
    private static final String FROM_EMAIL = dbProps.getProperty("mail.smtp.user");
    private static final String APP_PASSWORD = dbProps.getProperty("mail.smtp.app.password");

    /**
     * Sends an email asynchronously in a new thread to prevent blocking the execution flow.
     * This is useful for improving performance when sending emails during web requests.
     *
     * @param toEmail the recipient's email address
     * @param subject the subject line of the email
     * @param body the HTML content of the email body
     */
    public static void sendEmailAsync(String toEmail, String subject, String body) {
        new Thread(() -> sendEmail(toEmail, subject, body)).start();
    }
    /**
     * Sends an email using the Gmail SMTP server via the javax mail library.
     * Configures the session with STARTTLS and authenticates using application-specific credentials.
     *
     * @param toEmail the recipient's email address
     * @param subject the subject line of the email
     * @param body the HTML content of the email body
     */
    public static void sendEmail(String toEmail, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");
            message.setContent(body, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println("==> Đã gửi email thành công đến: " + toEmail);
        } catch (MessagingException e) {
            System.err.println("==> Lỗi gửi Email: " + e.getMessage());
        }
    }
    /**
     * Constructs and sends a formatted task reminder email to the user.
     * The email includes sections for overdue tasks, tasks due today, and upcoming tasks for tomorrow.
     * * @param user the recipient user object
     * @param overdue the list of tasks past their due date
     * @param today the list of tasks due on the current date
     * @param tomorrow the list of tasks due on the next day
     */
    public static void sendTaskReminder(User user, List<Task> overdue, List<Task> today, List<Task> tomorrow) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        sb.append("<h1>Chào ").append(user.getUsername()).append(",</h1>");
        sb.append("<p>Đây là tóm tắt danh sách công việc cần lưu ý của bạn.</p>");
        if (!overdue.isEmpty()) {
            renderTaskSection(sb, "🔴 Công việc quá hạn", overdue, true);
        }
        if (!today.isEmpty()) {
            renderTaskSection(sb, "📅 Hôm nay (Trước 0h)", today, false);
        }
        if (!tomorrow.isEmpty()) {
            String tomorrowDate = java.time.LocalDate.now().plusDays(1).toString();
            renderTaskSection(sb, "🟢 Ngày mai", tomorrow, false);
        }
        sb.append("<div><a href='http://localhost:8080/todolist_war_exploded/'>Xem chi tiết tại hệ thống</a></div>");
        sb.append("<p>Chúc bạn một ngày làm việc hiệu quả!</p>");
        sb.append("</div>");

        sendEmailAsync(user.getEmail(), "Thông báo công việc - " + user.getUsername(), sb.toString());
    }
    /**
     * Renders a specific section of the task list in HTML format.
     * Appends task titles, descriptions, and optionally due dates to the StringBuilder.
     *
     * @param sb the StringBuilder to which the HTML content is appended
     * @param title the title of the task section
     * @param tasks the list of tasks to be rendered
     * @param showDate whether to display the specific due date for each task
     */
    private static void renderTaskSection(StringBuilder sb, String title, List<Task> tasks, boolean showDate) {
        sb.append("<div>");
        sb.append("<h2>").append(title).append("</h2>");
        for (Task t : tasks) {
            sb.append("<div>");
            sb.append("<strong>").append(t.getTitle()).append("</strong>");
            if (t.getDescription() != null && !t.getDescription().isEmpty()) {
                sb.append(" - ").append(t.getDescription());
            }
            if (showDate && t.getDueDate() != null) {
                sb.append(" (Hạn: ").append(t.getDueDate()).append(")");
            }
            sb.append("</div>");
        }
        sb.append("</div>");
    }
}