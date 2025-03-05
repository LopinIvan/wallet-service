package com.lopinivan.walletservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lopinivan.walletservice.dto.OperationType;
import com.lopinivan.walletservice.dto.WalletTransactionRequest;
import com.lopinivan.walletservice.entity.Wallet;
import com.lopinivan.walletservice.exception.ErrorMessage;
import com.lopinivan.walletservice.exception.InsufficientFundsException;
import com.lopinivan.walletservice.mapper.WalletMapperImpl;
import com.lopinivan.walletservice.service.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@Import(WalletMapperImpl.class)
public class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    private static final String BASE_URL = "/api/v1/wallet";

    // Пустой запрос (валидация)
    @Test
    void testValidationError() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(ErrorMessage.VALIDATION_ERROR.getMessage()))
                .andExpect(jsonPath("$.message").exists()); // Проверяем, что есть описание ошибки
    }

    // Успешная транзакция (депозит)
    @Test
    void testSuccessfulTransaction() throws Exception {
        WalletTransactionRequest request = new WalletTransactionRequest(
                UUID.randomUUID(), OperationType.DEPOSIT, BigDecimal.valueOf(100));

        doNothing().when(walletService).updateBalance(any(UUID.class), any(BigDecimal.class), any(OperationType.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Transaction successful"));
    }

    // Ошибка: недостаточно средств
    @Test
    void testInsufficientFunds() throws Exception {
        WalletTransactionRequest request = new WalletTransactionRequest(
                UUID.randomUUID(), OperationType.WITHDRAW, BigDecimal.valueOf(500));

        doThrow(new InsufficientFundsException("Insufficient funds"))
                .when(walletService).updateBalance(any(UUID.class), any(BigDecimal.class), any(OperationType.class));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(ErrorMessage.INSUFFICIENT_FUNDS.getMessage()))
                .andExpect(jsonPath("$.message").value("Insufficient funds"));
    }

    // Успешное получение баланса
    @Test
    void testGetBalanceSuccess() throws Exception {
        UUID walletId = UUID.randomUUID();
        BigDecimal balance = new BigDecimal("100.0");
        Wallet wallet = new Wallet(walletId, balance, LocalDateTime.now(), LocalDateTime.now());
        when(walletService.getWallet(walletId)).thenReturn(Optional.of(wallet));

        mockMvc.perform(get(BASE_URL + "/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.walletId").value(walletId.toString()))
                .andExpect(jsonPath("$.balance").value(balance));
    }

    // Ошибка: кошелек не найден
    @Test
    void testGetBalanceNotFound() throws Exception {
        UUID walletId = UUID.randomUUID();
        when(walletService.getWallet(walletId)).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE_URL + "/" + walletId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value(ErrorMessage.WALLET_NOT_FOUND.getMessage()))
                .andExpect(jsonPath("$.message").value("Wallet not found with id: " + walletId));
    }
}