package com.codewithmosh.dryptoapi.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "network")
    private String network;

    @Column(name = "data_plan_code")
    private String dataPlanCode;

    @Column(name = "amount_naira")
    private BigDecimal amountNaira;

    @Column(name = "amount_crypto")
    private BigDecimal amountCrypto;

    @Column(name = "crypto_currency")
    private String cryptoCurrency;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status; // PENDING, PAID, COMPLETED, FAILED, EXPIRED

    @Column(name = "delivery_status")
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus; // PENDING, DELIVERED, FAILED
    //from vtpass
    @Column(name = "request_id")
    private String requestId;
//    from quidax
    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

//    @Column(name = ) //not needed, aim must be achieved
//    private LocalDateTime priceLockedAt;
}