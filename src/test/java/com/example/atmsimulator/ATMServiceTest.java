package com.example.atmsimulator;

// import testing tools
import org.junit.jupiter.api.Test; // JUnit for test runner
import org.junit.jupiter.api.extension.ExtendWith; // connects JUnit to Mockito
import org.mockito.InjectMocks; // injects the fake dependencies into the service
import org.mockito.Mock; // creates the fake repo
import org.mockito.junit.jupiter.MockitoExtension; // glue between JUnit and Mockito

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*; //
import static org.mockito.Mockito.*; //

// use Mockito toolkit
@ExtendWith(MockitoExtension.class)
public class ATMServiceTest {

    // building fake fridge
    // wait and tell what to return
    @Mock
    private AccountRepository accountRepository;

    // creates real DB
    // automatically inject DB which created above
    @InjectMocks
    private ATMService atmService;

    // Test 1. Successful withdrawal
    @Test
    void processWithdrawal_ShouldDeductBalance_WhenFundsAreSufficient() {
        // 1. ARRANGE (set up the fake scenarios)
        // creates a real account object
        Account fakeAccount = new Account();
        fakeAccount.setCardNumber("1234");
        fakeAccount.setBalance(new BigDecimal("500.00"));

        // The fake fridge
        when(accountRepository.findByCardNumber("1234")).thenReturn(Optional.of(fakeAccount));

        // ACT (perform the withdrawal)
        String authCode = atmService.processWithdrawal("1234", new BigDecimal("100.00"));

        // 3. ASSERT (check the results)
        assertEquals(new BigDecimal("400.00"), fakeAccount.getBalance());

        // authCode start with "AUTH-"
        assertTrue(authCode.startsWith("AUTH-"));

        // check if the chef actually called the fake fridge's save() method
        // proves the service is saving data correctly
        verify(accountRepository, times(1)).save(fakeAccount);
    }

    // Test 2. Insufficient Funds
    @Test
    void processWithdrawal_ShouldThrownError_WhenFundsAreInsufficient() {
        // 1. ARRANGE
        Account fakeAccount = new Account();
        fakeAccount.setBalance(new BigDecimal("50.00")); // user only has this amount

        when(accountRepository.findByCardNumber("1234")).thenReturn(Optional.of(fakeAccount));

        // 2. ACT & RESET
        assertThrows(RuntimeException.class, () -> {
            atmService.processWithdrawal("1234", new BigDecimal("100.00"));
        });
        // failed, balance should not have been saved
        verify(accountRepository, never()).save(fakeAccount);
    }

    // Test 3. Successful Deposit
    @Test
    void processDeposit_shouldAddBalance_WhenValidAccount() {
        // 1. ARRANGE
        Account fakeAccount = new Account();
        fakeAccount.setBalance(new BigDecimal("400.00")); // user has 400

        when(accountRepository.findByCardNumber("1234")).thenReturn(Optional.of(fakeAccount));

        // 2. ACT
        String authCode = atmService.processDeposit("1234", new BigDecimal("50.00"));

        // 3. ASSERT
        assertEquals(new BigDecimal("450.00"), fakeAccount.getBalance());
        assertTrue(authCode.startsWith("DEP-"));
        verify(accountRepository, times(1)).save(fakeAccount);
    }

    // Test 4. Account not found
    @Test
    void processWithdrawal_ShouldThrownError_WhenAccountNotFound() {
        // ARRANGE
        when(accountRepository.findByCardNumber("9999")).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThrows(RuntimeException.class, () -> {
            atmService.processWithdrawal("9999", new BigDecimal("10.00"));
        });

        // Verify save was NEVER called
        verify(accountRepository, never()).save(any());
    }

}
