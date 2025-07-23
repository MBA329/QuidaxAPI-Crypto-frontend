package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.PurchaseRequest;
import com.codewithmosh.dryptoapi.dtos.PurchaseResponse;
import com.codewithmosh.dryptoapi.entities.DeliveryStatus;
import com.codewithmosh.dryptoapi.entities.Transaction;
import com.codewithmosh.dryptoapi.entities.TransactionStatus;
import com.codewithmosh.dryptoapi.exceptions.NoActiveWalletException;
import com.codewithmosh.dryptoapi.exceptions.WalletNotFoundException;
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
        var activeWallets = walletRepository.findByCryptoCurrencyAndIsActiveFalse(request.getCryptoCurrency());
        if (activeWallets.isEmpty()) {
            throw new WalletNotFoundException();
        }
        var activeWallet = activeWallets.get(0);
//                .orElseThrow(NoActiveWalletException::new);

        var marketRate = paymentGateway.getBuyPrice(request.getCryptoCurrency().toLowerCase() + "ngn");

        var serviceId = request.getNetwork().toLowerCase() + "-data";
        var amountNaira = serviceGateway.getAmountNaira(request.getDataPlanCode(), serviceId);
        if (amountNaira == null)
        {
            return null;
        }
        var cryptoPrice = getCryptoPrice(marketRate.getData().getTicker().getBuy(), amountNaira);
        activeWallet.setActive(true);

//        walletRepository.save(activeWallet);
        var requestId = serviceGateway.generateRequestId();

        var transaction = Transaction.builder()
                .phoneNumber(request.getPhoneNumber())
                .network(request.getNetwork())
                .dataPlanCode(request.getDataPlanCode())
                .amountNaira(amountNaira)
                .cryptoCurrency(request.getCryptoCurrency())
                .amountCrypto(cryptoPrice)
                .serviceId(serviceId)
                .requestId(requestId)
                .wallet(activeWallet)
                .deliveryStatus(DeliveryStatus.PENDING)
                .transactionStatus(TransactionStatus.PENDING)
                .billersCode(request.getBillersCode())
                .isTerminated(false)
                .build();

        transaction.setExpiresAt(LocalDateTime.now().plusHours(1));

        transactionRepository.save(transaction);

        return transactionMapper.toPurchaseResponse(transaction);
    }

    private BigDecimal getCryptoPrice(BigDecimal cryptoBuyPrice, BigDecimal amountNaira) {
        cryptoBuyPrice = cryptoBuyPrice.add(cryptoBuyPrice.multiply(BigDecimal.valueOf(0.01)));
        return amountNaira.divide(cryptoBuyPrice, 8, RoundingMode.CEILING);
    }
//1btv = 200ngn
//    x = 50k0ngng
}
