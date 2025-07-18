package com.codewithmosh.dryptoapi.dtos;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class PurchaseResponse {
    private String dataPlanCode;
    private String depositAddress;
    private String network;
    private String cryptoCurrency;
    private BigDecimal amountCrypto;
    private BigDecimal amountNaira;
    private String requestId;
    private String phoneNumber;
    private String expiresAt; //added this
}
