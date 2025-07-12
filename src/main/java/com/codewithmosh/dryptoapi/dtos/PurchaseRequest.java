package com.codewithmosh.dryptoapi.dtos;

import com.codewithmosh.dryptoapi.entities.DeliveryStatus;
import com.codewithmosh.dryptoapi.entities.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class PurchaseRequest {

    private String phoneNumber;

    private String network;

    private String dataPlanCode;

    private BigDecimal amountNaira;

    private String cryptoCurrency;
}
