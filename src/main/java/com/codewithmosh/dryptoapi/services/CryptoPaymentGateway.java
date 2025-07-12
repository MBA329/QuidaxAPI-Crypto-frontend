package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.FetchWalletsResponse;
import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.dtos.FetchWalletResponse;

public interface CryptoPaymentGateway {
    public TickerResponse getBuyPrice(String marketPair);
    public FetchWalletResponse fetchCryptoWallet(String cryptoCurrency);
    public FetchWalletsResponse fetchPaymentAddresses(String cryptoCurrency);
    public FetchWalletResponse createPaymentAddress(String cryptoCurrency);
}
