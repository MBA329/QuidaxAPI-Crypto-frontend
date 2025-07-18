package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.data.AppContext;
import com.codewithmosh.dryptoapi.dtos.FetchWalletResponse;
import com.codewithmosh.dryptoapi.dtos.WalletResponse;
import com.codewithmosh.dryptoapi.exceptions.WalletNotFoundException;
import com.codewithmosh.dryptoapi.mappers.WalletMapper;
import com.codewithmosh.dryptoapi.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class WalletService {

    private final CryptoPaymentGateway paymentGateway;
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;


    public void loadWallets() {

        for (var i: AppContext.cryptos) {
            var wallet = paymentGateway.fetchPaymentAddresses(i);
            if (wallet == null) {
                continue;
            }
            for (var j : wallet.getWallets()) {
                var myWallet = walletMapper.toWallet(j);
                var walletExists = walletRepository.findById(myWallet.getId()).orElse(null);
                if (walletExists != null) {
                    continue;
                }
                walletRepository.save(myWallet);
            }

        }

    }

    @Transactional
    public void createWallets() {
        for (var i: AppContext.cryptos) {
            var addresses = paymentGateway.fetchPaymentAddresses(i);
            if (addresses == null) {
                continue;
            }
            var count = addresses.getWallets().size();
            int limit = 13;
            if (count < limit) {
                for (int j = count + 1; j <= limit; j++) {
//                    var wallet = walletMapper.toWallet(paymentGateway.createPaymentAddress(i));
//                    walletRepository.save(wallet);
                    paymentGateway.createPaymentAddress(i);
                }
            }

        }

    }

    public WalletResponse getWalletByCryptoCurrency(String cryptoCurrency) {
        var wallet = walletRepository.findByCryptoCurrency(cryptoCurrency).orElse(null);
        return walletMapper.toWalletResponse(wallet);
    }


    public WalletResponse getActiveWalletByCryptoCurrency(String cryptoCurrency) {
        var wallets = walletRepository.findByCryptoCurrencyAndIsActiveFalse(cryptoCurrency);
        if (wallets.isEmpty()) {
            throw new WalletNotFoundException();
        }
        var wallet = wallets.get(0);
        return walletMapper.toWalletResponse(wallet);
    }
    @Scheduled(cron = "0 0 * * * *") // every hour
    public void deactivateExpiredWallets() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusMinutes(30);

        int updated = walletRepository.deactivateWalletsWithExpiredTransactions(cutoff);
        System.out.println("✅ Deactivated " + updated + " wallet(s) with expired transactions.");
    }

//    public void createWallet(FetchWalletResponse walletRequest) {
//        var wallet = walletMapper.toWallet(walletRequest);
//        walletRepository.save(wallet);
//    }
}
