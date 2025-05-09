package org.bringme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/** To run the program locally
 * mvn spring-boot:run -Dspring-boot.run.profiles=dev
 */
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
