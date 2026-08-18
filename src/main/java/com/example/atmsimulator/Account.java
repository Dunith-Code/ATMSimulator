package com.example.atmsimulator;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity

@Table(name = "accounts")
public class Account {

    // Primary Key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Card Number
    @Column(unique = true, nullable = false)
    private String cardNumber;

    // PIN
    @Column(nullable = false)
    private String Pin;

    // Balance
    @Column(precision = 15, scale = 2)
    private BigDecimal balance;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
