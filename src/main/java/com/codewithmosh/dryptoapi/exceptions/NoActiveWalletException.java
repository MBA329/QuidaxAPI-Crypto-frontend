package com.codewithmosh.dryptoapi.exceptions;

public class NoActiveWalletException extends RuntimeException {
    public NoActiveWalletException() {
        super("No active wallet found");
    }
}
