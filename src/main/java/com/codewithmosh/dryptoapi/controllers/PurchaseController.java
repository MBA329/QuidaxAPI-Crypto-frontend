package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.services.CryptoPaymentGateway;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/purchase")
@AllArgsConstructor
public class PurchaseController {
    private final CryptoPaymentGateway paymentGateway;

    @GetMapping("/buy-price/{market_pair}")
    public ResponseEntity<TickerResponse> getBuyPrice(
            @PathVariable("market_pair") String marketPair
    ) {
        TickerResponse price = paymentGateway.getBuyPrice(marketPair);
        return ResponseEntity.ok(price);
    }
}
