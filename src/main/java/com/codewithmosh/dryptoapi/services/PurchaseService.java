package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.constants.MarketPair;
import com.codewithmosh.dryptoapi.dtos.PurchaseRequest;
import com.codewithmosh.dryptoapi.dtos.PurchaseResponse;
import com.codewithmosh.dryptoapi.entities.Transaction;
import com.codewithmosh.dryptoapi.exceptions.NoActiveWalletException;
import com.codewithmosh.dryptoapi.exceptions.WalletNotFoundException;
import com.codewithmosh.dryptoapi.repositories.TransactionRepository;
import com.codewithmosh.dryptoapi.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@AllArgsConstructor
public class PurchaseService {
    private final CryptoPaymentGateway paymentGateway;
    private final UtilityServiceGateway serviceGateway;
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public PurchaseResponse makeDataPurchase(PurchaseRequest request) {
        var activeWallet = walletRepository.findByCryptoCurrencyAndActiveTrue(request.getCryptoCurrency()).orElseThrow(throw new NoActiveWalletException(););
        var marketRate = paymentGateway.getBuyPrice(request.getCryptoCurrency().toLowerCase() + "ngn");
        var cryptoPrice = getCryptoPrice(marketRate.getData().getTicker().getBuy(), request.getAmountNaira());

        var transaction = Transaction.builder()
                .phoneNumber(request.getPhoneNumber())
                .network(request.getNetwork())
                .dataPlanCode(request.getDataPlanCode())
                .amountNaira(request.getAmountNaira())
                .cryptoCurrency(request.getCryptoCurrency())
                .amountCrypto(cryptoPrice)
                .wallet(activeWallet)
                .build();



        transactionRepository.save(transaction);


    }

    private BigDecimal getCryptoPrice(BigDecimal amount, BigDecimal amountNaira) {
        amount = amount.add(amount.multiply(BigDecimal.valueOf(0.01)));
        return amount.divide(amountNaira, 2, RoundingMode.CEILING);
    }

}
