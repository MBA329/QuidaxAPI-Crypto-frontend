package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.services.CryptoPaymentGateway;
import com.codewithmosh.dryptoapi.services.UtilityServiceGateway;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
@AllArgsConstructor
public class WebhookController {
    private final CryptoPaymentGateway paymentGateway;
    private final UtilityServiceGateway serviceGateway;
    //for testing
    @PostMapping("/quidax")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }

        String rawPayload = jsonBuilder.toString();
        paymentGateway.processWebhook(rawPayload);

        return ResponseEntity.ok("Webhook processed");
    }


    @PostMapping("/vtpass")
    public Map<String, String> handleVTPassWebhook(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        }
        String rawPayload = jsonBuilder.toString();
        serviceGateway.processWebhook(rawPayload);

        return Map.of("response", "success");
    }

}
