package com.example.atmsimulator;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

// Handle request (waiter)
@CrossOrigin(origins = "http://localhost:3000")
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

        // 1. extract all data from the request
        String cardNumber = request.get("cardNumber");
        String pin = request.get("pin"); // PIN extraction
        BigDecimal amount = new BigDecimal(request.get("amount"));

        try {
            // 2. call the service, which validates PIN internally
            String authCode = atmService.processWithdrawal(cardNumber, pin, amount);
            return Map.of("status", "SUCCESS", "authCode", authCode, "message", "Please collect your cash");
        } catch (RuntimeException e) {
            return Map.of("status", "FAILED", "message", e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public Map<String, String> deposit(@RequestBody Map<String, String> request) {
        String cardNumber = request.get("cardNumber");
        String nic = request.get("nic"); // NIC extraction
        BigDecimal amount = new BigDecimal(request.get("amount"));

        try {
            String authCode = atmService.processDeposit(cardNumber, nic, amount);
            return Map.of("status", "SUCCESS", "authCode", authCode, "message", "Deposit successful");
        } catch (RuntimeException e) {
            return Map.of("status", "FAILED", "message", e.getMessage());
        }
    }
}
