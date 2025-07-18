package com.codewithmosh.dryptoapi.exceptions;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException() {
        super("Wallet not found");
    }
}
