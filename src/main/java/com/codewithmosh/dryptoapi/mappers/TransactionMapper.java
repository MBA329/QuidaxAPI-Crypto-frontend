package com.codewithmosh.dryptoapi.mappers;

import com.codewithmosh.dryptoapi.dtos.PurchaseResponse;
import com.codewithmosh.dryptoapi.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "wallet.depositAddress", target = "depositAddress")
    @Mapping(source = "wallet.network", target = "network")
    @Mapping(source = "wallet.cryptoCurrency", target = "cryptoCurrency")
    PurchaseResponse toPurchaseResponse(Transaction transaction);
}
