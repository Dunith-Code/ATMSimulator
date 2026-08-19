package com.example.atmsimulator;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

// Business logic layer
@Service
public class ATMService {
    // Repo into service (fridge into chef)
    private final AccountRepository accountRepository;

    // constructor injection
    public ATMService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // withdraw money
    public String processWithdrawal(String cardNumber, BigDecimal amount) {

        // find the account
        Account account = accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // check enough money
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // cal new balance and update account
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        // save updated db
        accountRepository.save(account);

        // return fake approval code
        return "AUTH-" + System.currentTimeMillis();
    }
}
