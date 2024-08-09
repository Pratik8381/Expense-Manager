package com.service;
import com.Notification.NotificationService;
import com.model.Userr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private EmailService emailService; // Inject your email service implementation here

    @Autowired
    private UserService userService; // Inject your user service implementation here

    @Override
    @Scheduled(cron = "0 0 21 * * *") // Scheduled to run every day at 9 PM
    public void sendDailyNotifications() {
        // Fetch recipients (example: fetch all users)
        List<Userr> users = userService.getAllUsers();

        // Construct and send daily message
        for (Userr user : users) {
            BigDecimal totalExpenses = getTotalExpensesForUser(user);
            String message = "Dear " + user.getUname() + ",\n"
                    + "Here's your daily expense summary:\n"
                    + "Total expenses: $" + totalExpenses + "\n"
                    + "Have a productive evening!";
            sendNotification(user.getUemail(), message);
        }
    }

    private BigDecimal getTotalExpensesForUser(Userr user) {
        // Example: Logic to fetch total expenses for the user from database
        // Replace with your actual logic to calculate total expenses for the user
        // This is just a placeholder
        return BigDecimal.valueOf(1500.75); // Example total expenses
    }

    private void sendNotification(String recipient, String message) {
        // Example: Send email notification using injected email service
        emailService.sendEmail(recipient, "Daily Expense Summary", message);
    }
}
