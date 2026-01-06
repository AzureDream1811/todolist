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

/**
 * Listener class that manages background scheduling for task reminders.
 * It ensures that the system automatically scans and notifies users about their
 * tasks at regular intervals throughout the application lifecycle.
 */
@WebListener
public class TaskReminderScheduler implements ServletContextListener {
    private ScheduledExecutorService scheduler;

    /**
     * Initializes the scheduled executor service when the web context is started.
     * Sets up a recurring task with an initial delay and a fixed rate of 24 hours.
     * This ensures the reminder logic runs consistently every day.
     * * @param sce the ServletContextEvent containing the web application context
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        long initialDelay = 20;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                executeDailyReminder();
            } catch (Exception e) {
                System.err.println("==> [Scheduler Error]: " + e.getMessage());
            }
        }, initialDelay, 24, TimeUnit.HOURS);

        System.out.println("==> [System] Scheduler starts. Email will be sent later. " + initialDelay + " second");
    }

    /**
     * Executes the core logic for daily reminders by scanning all users in the database.
     * For each user, it retrieves overdue tasks, tasks due today, and tasks due tomorrow.
     * If any relevant tasks are found, it invokes EmailUtils to dispatch a reminder email.
     */
    private void executeDailyReminder() {
        System.out.println("==> [TaskScheduler] Scanning daily tasks...");
        TaskDAO taskDAO = DAOFactory.getInstance().getTaskDAO();
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

        List<User> users = userDAO.getAllUsers();
        if (users == null || users.isEmpty()) return;

        for (User user : users) {
            List<Task> overdue = taskDAO.getOverdueTaskByUserID(user.getId());
            List<Task> today = taskDAO.getTodayTaskByUserID(user.getId());
            List<Task> tomorrow = taskDAO.getTasksDueOn(user.getId(), LocalDate.now().plusDays(1));

            if (!overdue.isEmpty() || !today.isEmpty() || !tomorrow.isEmpty()) {
                EmailUtils.sendTaskReminder(user, overdue, today, tomorrow);
            }
        }
    }

    /**
     * Calculates the time difference in seconds from the current moment until 8:00 AM.
     * If the current time is already past 8:00 AM, it calculates the delay for 8:00 AM
     * on the following day to ensure the task runs at the correct scheduled time.
     * * @return the calculated delay in seconds until the next 8:00 AM
     */
    private long calculateDelayUntil8AM() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = now.withHour(8).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(target)) {
            target = target.plusDays(1);
        }
        return Duration.between(now, target).toSeconds();
    }

    /**
     * Shuts down the scheduled executor service when the web application is stopped.
     * This prevents potential memory leaks and ensures that background threads
     * are terminated gracefully during undeployment.
     * * @param sce the ServletContextEvent containing the web application context
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
            System.out.println("==> [System] Task scheduler has stopped");
        }
    }
}