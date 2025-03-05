package com.lopinivan.walletservice.mapper;

import com.lopinivan.walletservice.dto.WalletBalanceResponse;
import com.lopinivan.walletservice.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapperImpl {

    @Mapping(source = "id", target = "walletId")
    WalletBalanceResponse toWalletBalanceResponse(Wallet wallet);
}

