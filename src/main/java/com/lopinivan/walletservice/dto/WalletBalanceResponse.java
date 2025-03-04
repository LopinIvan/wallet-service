package com.lopinivan.walletservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletBalanceResponse {

    @NotNull
    private UUID walletId;

    @NotNull
    private BigDecimal balance;

}
