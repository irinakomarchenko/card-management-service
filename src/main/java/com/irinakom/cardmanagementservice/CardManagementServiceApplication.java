package com.irinakom.cardmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CardManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CardManagementServiceApplication.class, args);
    }
}
