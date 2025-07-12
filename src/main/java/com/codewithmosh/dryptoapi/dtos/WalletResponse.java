package com.codewithmosh.dryptoapi.dtos;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.UUID;

@Data
public class WalletResponse {
    private String id;

    private String depositAddress;

    private String cryptoCurrency;
    private String network;

}
