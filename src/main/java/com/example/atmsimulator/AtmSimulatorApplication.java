package com.example.atmsimulator;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.math.BigDecimal;

@SpringBootApplication
public class AtmSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AtmSimulatorApplication.class, args);
    }

    // Define the Bean and ATMService can use it
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner initDatabase(AccountRepository repo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (repo.findByCardNumber("1234-5678-9012").isEmpty()) {
                Account testAccount = new Account();
                testAccount.setCardNumber("1234-5678-9012");
                testAccount.setPin(passwordEncoder.encode("1234"));
                testAccount.setNic("123456789V");
                testAccount.setBalance(new BigDecimal("500.00"));

                repo.save(testAccount);
                System.out.println("Success! Account inserted with default PIN 1234");
            } else {
                System.out.println("Account already exists");
            }
        };
    }

}
