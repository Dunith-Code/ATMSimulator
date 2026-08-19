package com.example.atmsimulator;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

// Handle request (waiter)
@RestController
@RequestMapping("/api/atm")
public class ATMController {

    private final ATMService atmService;

    public ATMController(ATMService atmService) {
        this.atmService = atmService;
    }

    // listen for POST request
    @PostMapping("/withdraw")
    public Map<String, String>withdraw(@RequestBody Map<String, String> request) {

        String cardNumber = request.get("cardNumber");
        BigDecimal amount = new BigDecimal(request.get("amount"));

        try {
            String authCode = atmService.processWithdrawal(cardNumber, amount);
            return Map.of("status", "SUCCESS", "authCode", authCode, "message", "Please collect your cash");
        } catch (RuntimeException e) {
            return Map.of("status", "FAILED", "message", e.getMessage());
        }
    }
}
