package web.utils;

import web.dao.DAOFactory;
import web.dao.TaskDAO;
import web.dao.UserDAO;
import web.model.Task;
import web.model.User;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class TaskReminderScheduler implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();

        // Tính toán thời gian từ bây giờ đến 8h sáng mai
        //long initialDelay = calculateDelayUntil8AM();
        long initialDelay = 20;

        // Chạy định kỳ mỗi 24 tiếng (mỗi ngày vào lúc 8h sáng)
        scheduler.scheduleAtFixedRate(() -> {
            try {
                executeDailyReminder();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, 24, TimeUnit.HOURS);

        System.out.println("==> [System] TEST MODE: Email sẽ gửi sau 20 giây nữa.");
        //System.out.println("==> [System] Đã đặt lịch nhắc nhở lúc 8:00 AM hàng ngày.");
    }

    // Trong phương thức executeDailyReminder của TaskReminderScheduler.java
    private void executeDailyReminder() {
        System.out.println("==> [DEBUG] Đang bắt đầu quét Task..."); // Thêm dòng này
        TaskDAO taskDAO = DAOFactory.getInstance().getTaskDAO();
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        List<User> users = userDAO.getAllUsers();

        System.out.println("==> [DEBUG] Số lượng User tìm thấy: " + (users != null ? users.size() : 0));

        for (User user : users) {
            System.out.println("==> [DEBUG] Đang check cho User: " + user.getUsername());

            List<Task> overdueTasks = taskDAO.getOverdueTaskByUserID(user.getId());
            List<Task> todayTasks = taskDAO.getTodayTaskByUserID(user.getId());
            List<Task> tomorrowTasks = taskDAO.getTasksDueOn(user.getId(), LocalDate.now().plusDays(1));

            System.out.println("==> [DEBUG] " + user.getUsername() + ": Overdue=" + overdueTasks.size() +
                    ", Today=" + todayTasks.size() + ", Tomorrow=" + tomorrowTasks.size());

            if (!overdueTasks.isEmpty() || !todayTasks.isEmpty() || !tomorrowTasks.isEmpty()) {
                System.out.println("==> [DEBUG] Đang thực hiện gửi mail cho: " + user.getEmail());
                // Dùng sendEmail (không Async) để xem lỗi ngay lập tức nếu có
                EmailUtils.sendEmail(user.getEmail(), "Daily Task Summary", buildEnhancedEmailTemplate(user.getUsername(), overdueTasks, todayTasks, tomorrowTasks));
            }
        }
    }

    private String buildEnhancedEmailTemplate(String name, List<Task> overdue, List<Task> today, List<Task> tomorrow) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='font-family: Arial, sans-serif; line-height: 1.6;'>");
        sb.append("<h2 style='color: #2c3e50;'>Hi ").append(name).append(",</h2>");
        sb.append("<p>Here is your task summary for today, ").append(LocalDate.now()).append(":</p>");

        if (!overdue.isEmpty()) {
            sb.append("<h3 style='color: #e74c3c;'>🔴 Overdue Tasks</h3><ul>");
            for (Task t : overdue) {
                sb.append("<li><b>").append(t.getTitle()).append("</b> (Due: ").append(t.getDueDate()).append(")</li>");
            }
            sb.append("</ul>");
        }

        if (!today.isEmpty()) {
            sb.append("<h3 style='color: #f39c12;'>🟠 Due Today (Before Midnight)</h3><ul>");
            for (Task t : today) {
                sb.append("<li>").append(t.getTitle()).append("</li>");
            }
            sb.append("</ul>");
        }

        if (!tomorrow.isEmpty()) {
            sb.append("<h3 style='color: #27ae60;'>🟢 Coming Up Tomorrow (").append(LocalDate.now().plusDays(1)).append(")</h3><ul>");
            for (Task t : tomorrow) {
                sb.append("<li>").append(t.getTitle()).append("</li>");
            }
            sb.append("</ul>");
        }

        sb.append("<br><p>Log in to your account to update your progress!</p>");
        sb.append("</div>");
        return sb.toString();
    }

    private long calculateDelayUntil8AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.withHour(8).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(target)) {
            target = target.plusDays(1);
        }
        return Duration.between(now, target).toSeconds();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) scheduler.shutdownNow();
    }
}