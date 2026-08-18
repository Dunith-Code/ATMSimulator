package com.example.atmsimulator;

// Jpa Repo. tools
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// Acc.repo. Interface, inheritance
public interface AccountRepository extends JpaRepository<Account, Long>{

    //find an account by card number
    Optional<Account> findByCardNumber(String cardNumber);
}
