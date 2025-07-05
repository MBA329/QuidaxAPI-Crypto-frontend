package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.dtos.FetchWalletResponse;

public interface CryptoPaymentGateway {
    public TickerResponse getBuyPrice(String marketPair);
    public FetchWalletResponse fetchCryptoWallet(String cryptoCurrency);
}
