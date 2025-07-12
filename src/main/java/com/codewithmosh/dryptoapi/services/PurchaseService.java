package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.PurchaseRequest;
import com.codewithmosh.dryptoapi.dtos.PurchaseResponse;
import com.codewithmosh.dryptoapi.entities.DeliveryStatus;
import com.codewithmosh.dryptoapi.entities.Transaction;
import com.codewithmosh.dryptoapi.entities.TransactionStatus;
import com.codewithmosh.dryptoapi.exceptions.NoActiveWalletException;
import com.codewithmosh.dryptoapi.mappers.TransactionMapper;
import com.codewithmosh.dryptoapi.repositories.TransactionRepository;
import com.codewithmosh.dryptoapi.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PurchaseService {
    private final CryptoPaymentGateway paymentGateway;
    private final UtilityServiceGateway serviceGateway;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public PurchaseResponse makeDataPurchase(PurchaseRequest request) {
        var activeWallet = walletRepository.findByCryptoCurrencyAndActiveFalse(request.getCryptoCurrency())
                .orElseThrow(NoActiveWalletException::new);
        var marketRate = paymentGateway.getBuyPrice(request.getCryptoCurrency().toLowerCase() + "ngn");
        var cryptoPrice = getCryptoPrice(marketRate.getData().getTicker().getBuy(), request.getAmountNaira());
        activeWallet.setActive(false);
        var requestId = serviceGateway.generateRequestId();

        var transaction = Transaction.builder()
                .phoneNumber(request.getPhoneNumber())
                .network(request.getNetwork())
                .dataPlanCode(request.getDataPlanCode())
                .amountNaira(request.getAmountNaira())
                .cryptoCurrency(request.getCryptoCurrency())
                .amountCrypto(cryptoPrice)
                .requestId(requestId)
                .wallet(activeWallet)
                .deliveryStatus(DeliveryStatus.PENDING)
                .transactionStatus(TransactionStatus.PENDING)
                .billersCode(request.getServiceId())
                .build();

        transaction.setExpiresAt(LocalDateTime.now().plusHours(1));

        transactionRepository.save(transaction);

        return transactionMapper.toPurchaseResponse(transaction);
    }

    private BigDecimal getCryptoPrice(BigDecimal amount, BigDecimal amountNaira) {
        amount = amount.add(amount.multiply(BigDecimal.valueOf(0.01)));
        return amount.divide(amountNaira, 2, RoundingMode.CEILING);
    }

}
