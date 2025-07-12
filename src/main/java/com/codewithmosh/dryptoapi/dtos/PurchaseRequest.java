package com.codewithmosh.dryptoapi.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {

    private String phoneNumber;

    private String network;

    private String serviceId;

    private String billersCode;

    private String dataPlanCode;

    private BigDecimal amountNaira;

    private String cryptoCurrency;
}
