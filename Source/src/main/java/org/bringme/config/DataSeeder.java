package org.bringme.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


/**
 * Seed class to insert some data in the database after migrations file implemented
 * Output for every function will be provided after running the application in 'dev' profile
 * */
@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DataSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        seedPersons();
        seedTrips();
        seedItems();
        seedRequests();
    }

    /**
     * Insert data into Person table.
     * Set a valid email in the email field (it will be used in verification)
     * #Passwrod = 12345678
     * */
    public void seedPersons() {
        // Check if the 'users' table is empty
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM persons", Long.class);
            if (count == null || count == 0) {
                // Insert new data
                String sql = "INSERT INTO persons (first_name, last_name, address, email, phone, password, account_status, verification, role)" +
                        "VALUES" +
                        "('Passenger', 'seed', 'Passenger address', 'none@none.com', '123123', '$2a$10$pLZOXjzMO6wMrrzZOci9JeNZHr425gc1KLqeQms1Q5klXEvbJzzkO', 1, 1, 'User')," +
                        "('Sender', 'seed', 'Sender address', 'nonee@none.com', '123123', '$2a$10$pLZOXjzMO6wMrrzZOci9JeNZHr425gc1KLqeQms1Q5klXEvbJzzkO', 1, 1, 'User')," +
                        "('Admin', 'seed', 'Admin address', 'admin@admin.com', 123123, '$2a$10$pLZOXjzMO6wMrrzZOci9JeNZHr425gc1KLqeQms1Q5klXEvbJzzkO', 1, 1, 'Admin')";
                // Password is 12345678s
                jdbcTemplate.update(sql);
                System.out.println("Development person data has been seeded.");
            } else {
                System.out.println("Data already exists.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert data into Trips table
    * */
    public void seedTrips() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM trips", Long.class);
            if (count == null || count == 0) {
                String sql = "INSERT INTO trips (origin, destination, destination_airport, empty_weight, departure_time, arrival_time, transit, comments, passenger_id) " +
                        "VALUES " +

                        // First record
                        "(1, 2, 'Airport', 2, (NOW() + INTERVAL '2 years' + INTERVAL '1 day')::TIMESTAMP, (NOW() + INTERVAL '2 years' + INTERVAL '3 day')::TIMESTAMP, " +
                        "true, 'seed comment', (SELECT id FROM persons WHERE email = 'none@none.com'))," +

                        // Second record
                        "(3, 4, 'Airport', 2, (NOW() + INTERVAL '2 years' + INTERVAL '1 day')::TIMESTAMP, (NOW() + INTERVAL '2 years' + INTERVAL '3 day')::TIMESTAMP, " +
                        "true, 'seed comment', (SELECT id FROM persons WHERE email = 'none@none.com'))";

                jdbcTemplate.update(sql);
                System.out.println("Development trips data has been seeded.");
            } else {
                System.out.println("Data already exists.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert data into Items table
     */

    public void seedItems() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM items", Long.class);
            if (count == null || count == 0) {
                String sql = "INSERT INTO items (name, origin, destination, user_id, weight, height, length, full_address, comments, photo)" +
                        "VALUES " +
                        "('items1', 1, 2, (SELECT id FROM persons WHERE email = 'nonee@none.com'), 1, 1, 1, 'Full Address seed', 'seed comment', 'ITEM_item1-3e3ffef9-0754-4926-99f8-eee363b57def-1729650538812-.png')," +
                        "('items2', 3, 4, (SELECT id FROM persons WHERE email = 'nonee@none.com'), 1, 1, 1, 'Full Address seed', 'seed comment', 'ITEM_item1-0669716b-9a26-4348-950b-9d5584b9910d-1729576564685-.png')";
                jdbcTemplate.update(sql);
                System.out.println("Development items data has been seeded.");
            } else {
                System.out.println("Data already exists.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert data into Requests table
     * */
    public void seedRequests() {
        try {
            Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM requests", Long.class);
            if (count == null || count == 0) {
                String sql = "INSERT INTO requests(requester_user_id, requested_user_id, item_id, trip_id, origin, destination, comments, price, currency)" +
                        "VALUES " +
                        "((SELECT id FROM persons WHERE email = 'none@none.com'), " +
                        "(SELECT id FROM persons WHERE email = 'nonee@none.com'), " +
                        "(SELECT id FROM items WHERE name = 'items1' AND user_id = (SELECT id FROM persons WHERE email = 'nonee@none.com')), " +
                        "(SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') LIMIT 1), " +
                        "(SELECT origin FROM trips WHERE id = (SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') LIMIT 1)), " +
                        "(SELECT destination FROM trips WHERE id = (SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') LIMIT 1)), " +
                        "'Seed comment', 2.2, 'Dollar')," +
                        "((SELECT id FROM persons WHERE email = 'nonee@none.com'), " +
                        "(SELECT id FROM persons WHERE email = 'none@none.com'), " +
                        "(SELECT id FROM items WHERE name = 'items2' AND user_id = (SELECT id FROM persons WHERE email = 'nonee@none.com')), " +
                        "(SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') OFFSET 1 LIMIT 1), " +
                        "(SELECT origin FROM trips WHERE id = (SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') OFFSET 1 LIMIT 1)), " +
                        "(SELECT destination FROM trips WHERE id = (SELECT id FROM trips WHERE passenger_id = (SELECT id FROM persons WHERE email = 'none@none.com') OFFSET 1 LIMIT 1)), " +
                        "'Seed comment', 2.2, 'Dollar')";

                jdbcTemplate.update(sql);
                System.out.println("Development requests data has been seeded.");
            } else {
                System.out.println("Data already exists.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
