package com.example.atmsimulator;

// Jpa Repo. tools
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

// Acc.repo. Interface, inheritance
public interface AccountRepository extends JpaRepository<Account, Long>{

    //find an account by card number
    Optional<Account> findByCardNumber(String cardNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.cardNumber = :cardNumber")
    Optional<Account> findByCardNumberWithLock(@Param("cardNumber") String cardNumber);
}
