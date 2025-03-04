package com.lopinivan.walletservice.service;

import com.lopinivan.walletservice.dto.OperationType;
import com.lopinivan.walletservice.entity.Wallet;
import com.lopinivan.walletservice.exception.InsufficientFundsException;
import com.lopinivan.walletservice.exception.InvalidOperationTypeException;
import com.lopinivan.walletservice.exception.WalletNotFoundException;
import com.lopinivan.walletservice.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public void updateBalance(UUID walletId, BigDecimal amount, OperationType type) {

        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        switch (type) {
            case DEPOSIT -> wallet.setBalance(wallet.getBalance().add(amount));
            case WITHDRAW -> {
                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
            }
            default -> throw new InvalidOperationTypeException("Unsupported operation type");
        }
//        walletRepository.save(wallet);
    }

    public Optional<Wallet> getWallet(UUID walletId) {
        return walletRepository.findById(walletId);
    }
}
