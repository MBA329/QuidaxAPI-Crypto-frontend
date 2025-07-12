package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.data.AppContext;
import com.codewithmosh.dryptoapi.dtos.WalletResponse;
import com.codewithmosh.dryptoapi.exceptions.WalletNotFoundException;
import com.codewithmosh.dryptoapi.mappers.WalletMapper;
import com.codewithmosh.dryptoapi.repositories.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
                throw new WalletNotFoundException();
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
            var count = paymentGateway.fetchPaymentAddresses(i).getWallets().size();
            if (count >= 10) {
                continue;
            }
            else {
                for (int j = count + 1; j <= 10; j++) {
                    var wallet = walletMapper.toWallet(paymentGateway.createPaymentAddress(i));
                    walletRepository.save(wallet);
                }
            }

        }

    }

    public WalletResponse getWalletByCryptoCurrency(String cryptoCurrency) {
        var wallet = walletRepository.findByCryptoCurrency(cryptoCurrency).orElse(null);
        return walletMapper.toWalletResponse(wallet);
    }


    public WalletResponse getActiveWalletByCryptoCurrency(String cryptoCurrency) {
        var wallet = walletRepository.findByCryptoCurrencyAndActiveFalse(cryptoCurrency).orElse(null);
        if (wallet == null) {
            throw new WalletNotFoundException();
        }
        return walletMapper.toWalletResponse(wallet);
    }

//
//    public WalletResponse createWallet(WalletRequest request) {
//
//    }
//
//    public List<WalletResponse> getAllWallets() {
//
//    }

}
