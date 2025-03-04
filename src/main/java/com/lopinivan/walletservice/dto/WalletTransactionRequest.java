package com.lopinivan.walletservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletTransactionRequest {
    @NotNull
    private UUID walletId;

    @NotNull
    private OperationType operationType;

    @NotNull
    @Positive
    private BigDecimal amount;
}
