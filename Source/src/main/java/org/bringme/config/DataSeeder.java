package org.bringme.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("dev") // Only when it is dev profile
public class DataSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed persons table with one data
        seedPersons();

    }

    public void seedPersons() {
        // Check if the 'users' table is empty
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM persons", Long.class);
            if (count == null || count == 0) {
                // Insert new data
                String sql = "INSERT INTO persons (first_name, last_name, address, email, phone, password, account_status)" +
                        "VALUES" +
                        "('Seed', 'seed', 'seed,seed32', 'seed@seed.seed', '123123', '$2a$12$GkCudTUDIIUJH9q0vP6OA.Fn2YbXVKryjjDojfN8N5WMEHlhLN9dK', 1)";
                jdbcTemplate.update(sql);
                System.out.println("Development data has been seeded.");
            } else {
                System.out.println("Data already exists.");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
