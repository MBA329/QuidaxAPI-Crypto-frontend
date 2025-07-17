package com.codewithmosh.dryptoapi.mappers;

import com.codewithmosh.dryptoapi.dtos.FetchWalletResponse;
import com.codewithmosh.dryptoapi.dtos.WalletResponse;
import com.codewithmosh.dryptoapi.entities.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WalletMapper {
    WalletResponse toWalletResponse(Wallet wallet);

    @Mapping(source = "cryptoCurrency", target = "cryptoCurrency")
    @Mapping(source = "depositAddress", target = "depositAddress")
    @Mapping(source = "network", target = "network")
    @Mapping(source = "id", target = "id")
    Wallet toWallet(FetchWalletResponse fetchWalletResponse);
}