package com.Notification;
import com.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private EmailService emailService;

    // This method will run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendDailyEmail() {
        String to = "recipient@example.com";
        String subject = "Daily Report";
        String text = "This is your daily report email.";

        emailService.sendEmail(to, subject, text);
        System.out.println("Scheduled task at 9 AM - Email sent");
    }
}
