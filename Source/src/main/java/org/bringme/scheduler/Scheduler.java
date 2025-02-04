package org.bringme.scheduler;

import org.bringme.utils.DatabaseCleaner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class Scheduler {
    private final DatabaseCleaner databaseCleaner;

    public Scheduler(DatabaseCleaner databaseCleaner) {
        this.databaseCleaner = databaseCleaner;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void daily() {
        try {
            databaseCleaner.cleanExpiredVerificationCode();
        } catch (SQLException e) {
            System.out.println("Couldn't clean expired codes\n" + e.getMessage());
        }
    }
}
