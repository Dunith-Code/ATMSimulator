package com.example.atmsimulator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

// Business logic layer
@Service
public class ATMService {

    // Repo into service (fridge into chef)
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // constructor injection
    public ATMService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // withdrawal - validate the PIN
    private void validatePin(String cardNumber, String rawPin) {
        Account account = accountRepository.findByCardNumberWithLock(cardNumber)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!passwordEncoder.matches(rawPin, account.getPin())) {
            throw new RuntimeException("Invalid Pin");
        }
    }

    // deposit - validate the NIC
    private void validateNic(String cardNumber, String inputNic) {
        Account account = accountRepository.findByCardNumberWithLock(cardNumber)
            .orElseThrow(() -> new RuntimeException("Account not found"));
        
        // trim whitespace and ignore case for comparison
        String cleanInputNic = inputNic.trim();
        String storedNic = account.getNic().trim();
        
        // DEBUG: Print both values to see the mismatch
        System.out.println("DEBUG: Input NIC = '" + inputNic + "'");
        System.out.println("DEBUG: Stored NIC = '" + account.getNic() + "'");
        System.out.println("DEBUG: Equals check = " + account.getNic().equals(inputNic));

        if (!storedNic.equalsIgnoreCase(cleanInputNic)) {
            throw new RuntimeException("Invalid NIC");
        }
    }

    // Fraud Detection Rules
    private void checkForFraud(Account account, BigDecimal amount) {
        
        // Rule 1. Daily Limit
        if (amount.compareTo(new BigDecimal("1000")) > 0) {
            throw new RuntimeException("FRAUD ALERT: Amount exceeds daily limit");
        }

        // Rule 2. Suspicious large withdrawal
        BigDecimal halfBalance = account.getBalance().multiply(new BigDecimal("0.5"));
        if (amount.compareTo(halfBalance) > 0 && account.getBalance().compareTo(new BigDecimal("500")) > 0) {
            throw new RuntimeException("FRAUD ALERT: Unusually large withdrwal detected");
        }
    }

    // withdraw money and whole process runs in single DB
    @Transactional
    public String processWithdrawal(String cardNumber, String pin,BigDecimal amount) {
        // 1. validate the PIM
        validatePin(cardNumber, pin);

        // 2. find the account with a lock
        Account account = accountRepository.findByCardNumberWithLock(cardNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // 3. fraud detection
        checkForFraud(account, amount);

        // 4. check enough money
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient funds");
        }

        // 5. update balance
        account.setBalance(account.getBalance().subtract(amount));
        // account.setBalance(newBalance);

        // save updated db
        accountRepository.save(account);

        // return fake approval code
        return "AUTH-" + System.currentTimeMillis();
    }

    // put money back in
    @Transactional
    public String processDeposit(String cardNumber, String nic,BigDecimal amount) {
        // 1. validate the NIC
        validateNic(cardNumber, nic);

        // 2. fetch the account with a lock
        Account account = accountRepository.findByCardNumberWithLock(cardNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        // 3. limit check
        if (amount.compareTo(new BigDecimal("5000")) > 0) {
            System.out.println("Large deposit detected: " + amount  + " for nic: " + nic);
        }

        // 4. update balance
        account.setBalance(account.getBalance().add(amount));
        // account.setBalance(newBalance);
        accountRepository.save(account);

        return "DEP-" + System.currentTimeMillis();
    }
}
