package org.bringme.scheduler;

import org.bringme.utils.DatabaseCleaner;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.logging.Logger;

@Component
public class Scheduler {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Scheduler.class);
    private final DatabaseCleaner databaseCleaner;
    private final Logger logger = Logger.getLogger(Scheduler.class.getName());
    public Scheduler(DatabaseCleaner databaseCleaner) {
        this.databaseCleaner = databaseCleaner;
    }

    /**
     * Repeat called functions everyday
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void daily() {
        try {
            databaseCleaner.cleanExpiredVerificationCode();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
