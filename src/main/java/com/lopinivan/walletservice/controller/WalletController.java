package com.lopinivan.walletservice.controller;

import com.lopinivan.walletservice.dto.WalletBalanceResponse;
import com.lopinivan.walletservice.dto.WalletTransactionRequest;
import com.lopinivan.walletservice.exception.WalletNotFoundException;
import com.lopinivan.walletservice.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping
    public ResponseEntity<?> updateBalance(@RequestBody @Valid WalletTransactionRequest request) {
        walletService.updateBalance(request.getWalletId(), request.getAmount(), request.getOperationType());
        return ResponseEntity.ok(Map.of("message", "Transaction successful"));
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<?> getBalance(@PathVariable UUID walletId) {
        return walletService.getWallet(walletId)
                .map(wallet -> ResponseEntity.ok(new WalletBalanceResponse(walletId, wallet.getBalance())))
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found with id: " + walletId));
    }
}
