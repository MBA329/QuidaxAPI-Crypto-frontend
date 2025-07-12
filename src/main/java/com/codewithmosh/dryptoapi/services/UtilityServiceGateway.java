package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.NetworkContentDto;
import com.codewithmosh.dryptoapi.dtos.VTPassPurchaseRequest;
import com.codewithmosh.dryptoapi.dtos.VTPassPurchaseResponse;
import com.codewithmosh.dryptoapi.dtos.VTPassQueryTransactionResponse;

public interface UtilityServiceGateway {
    String generateRequestId();
    NetworkContentDto getVariationCodes(String serviceId);
    VTPassPurchaseResponse purchaseProduct(VTPassPurchaseRequest request);
    VTPassQueryTransactionResponse fetchTransactionStatus(String requestId);
    public void processWebhook(String rawPayload);
}
