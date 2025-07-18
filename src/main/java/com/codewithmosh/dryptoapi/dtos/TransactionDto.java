package com.codewithmosh.dryptoapi.dtos;

import com.codewithmosh.dryptoapi.entities.DeliveryStatus;
import com.codewithmosh.dryptoapi.entities.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDto {
    private Long id;

    private String phoneNumber;

    private String network;

    private String dataPlanCode;

    private BigDecimal amountNaira;

    private BigDecimal amountCrypto;

    private String cryptoCurrency;

    private WalletDto wallet;

    private TransactionStatus status; // PENDING, PAID, COMPLETED, FAILED, EXPIRED

    private DeliveryStatus deliveryStatus; // PENDING, DELIVERED, FAILED

    private String requestId;

    private String transactionHash;

    private LocalDateTime createdAt;
}
