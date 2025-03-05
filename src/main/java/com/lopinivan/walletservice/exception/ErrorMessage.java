package com.lopinivan.walletservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    WALLET_NOT_FOUND("Wallet Not Found"),
    INSUFFICIENT_FUNDS("Insufficient Funds"),
    VALIDATION_ERROR("Validation Error"),
    INVALID_OPERATION("Invalid Operation"),
    INVALID_JSON_FORMAT("Invalid JSON Format"),
    INTERNAL_SERVER_ERROR("Internal Server Error"),
    MISSING_PARAMETER("Missing Parameter"),
    TYPE_MISMATCH("Type Mismatch"),
    GENERAL_ERROR("Internal Server Error");


    private final String message;

}
