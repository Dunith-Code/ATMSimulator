package com.example.atmsimulator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class AtmSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmSimulatorApplication.class, args);
    }

    // run code immediately when the server starts
    @Bean
    public CommandLineRunner initDatabase(AccountRepository repo) {
        return args -> {

            if (repo.findByCardNumber("1234-5678-9012").isEmpty()) {
                // Create new acc
                Account testAccount = new Account();
                testAccount.setCardNumber("1234-5678-9012");
                testAccount.setPin("1234");
                testAccount.setBalance(new BigDecimal("500.00"));

                // Save
                repo.save(testAccount);
                System.out.println("Success! Account inserted into PostgreSQL");
            } else {
                System.out.println("Account already exists");
            }
        };
    }

}
