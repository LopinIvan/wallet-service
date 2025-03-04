package com.lopinivan.walletservice.service;

import com.lopinivan.walletservice.dto.OperationType;
import com.lopinivan.walletservice.entity.Wallet;
import com.lopinivan.walletservice.exception.InsufficientFundsException;
import com.lopinivan.walletservice.exception.WalletNotFoundException;
import com.lopinivan.walletservice.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    private UUID walletId;
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        walletId = UUID.randomUUID();
        wallet = new Wallet(walletId, BigDecimal.valueOf(100)
                , LocalDateTime.now(), LocalDateTime.now());
    }

    // Пополнение баланса кошелька
    @Test
    void testUpdateBalance_Deposit() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, BigDecimal.valueOf(50), OperationType.DEPOSIT);

        assertEquals(BigDecimal.valueOf(150), wallet.getBalance());
    }

    // Успешное снятие при достаточном балансе
    @Test
    void testUpdateBalance_Withdraw_Success() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        walletService.updateBalance(walletId, BigDecimal.valueOf(50), OperationType.WITHDRAW);

        assertEquals(BigDecimal.valueOf(50), wallet.getBalance());
    }

    // Недостаточно средств на балансе
    @Test
    void testUpdateBalance_Withdraw_InsufficientFunds() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        assertThrows(InsufficientFundsException.class, () ->
                walletService.updateBalance(walletId, BigDecimal.valueOf(200), OperationType.WITHDRAW)
        );
    }

    // Кошелек не найден при обновлении баланса
    @Test
    void testUpdateBalance_WalletNotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThrows(WalletNotFoundException.class, () ->
                walletService.updateBalance(walletId, BigDecimal.valueOf(50), OperationType.DEPOSIT)
        );
    }

    // Кошелек найден при запросе
    @Test
    void testGetWallet_Found() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));

        Optional<Wallet> result = walletService.getWallet(walletId);

        assertTrue(result.isPresent());
        assertEquals(wallet, result.get());
    }

    // Кошелек не найден при запросе
    @Test
    void testGetWallet_NotFound() {
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        Optional<Wallet> result = walletService.getWallet(walletId);

        assertFalse(result.isPresent());
    }
}
