package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.dtos.VTPassPurchaseRequest;
import com.codewithmosh.dryptoapi.services.CryptoPaymentGateway;
import com.codewithmosh.dryptoapi.services.UtilityServiceGateway;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
@AllArgsConstructor
public class PurchaseController {
    private final CryptoPaymentGateway paymentGateway;
    private final UtilityServiceGateway serviceGateway;

    @GetMapping("/buy-price/{market_pair}")
    public ResponseEntity<TickerResponse> getBuyPrice(
            @PathVariable("market_pair") String marketPair
    ) {
        TickerResponse price = paymentGateway.getBuyPrice(marketPair);
        return ResponseEntity.ok(price);
    }

    @PostMapping("/fetch-plans/{serviceProvider}")
    public ResponseEntity<?> fetchDataPlans(@PathVariable("serviceProvider") String networkName) {
        var response = serviceGateway.getVariationCodes(networkName.toLowerCase() + "-data");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy-data}")
    public ResponseEntity<?> buyData(
            @RequestBody VTPassPurchaseRequest request
    ) {
        var response = serviceGateway.purchaseProduct(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify/{request_id}")
    public ResponseEntity<?> verifyPurchase(
            @PathVariable("request_id") String requestId
    ) {
        var response = serviceGateway.fetchTransactionStatus(requestId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy-data/{variation_code}")
    public ResponseEntity<?> buyData(
            @PathVariable("variation_code") String variationCode
    ) {
        var request = new VTPassPurchaseRequest(
                serviceGateway.generateRequestId(),
                "airtel-data",
                "09079275768",
                variationCode,
                "08011111111");
        var response = serviceGateway.purchaseProduct(request);
        return ResponseEntity.ok(response);
    }
}
