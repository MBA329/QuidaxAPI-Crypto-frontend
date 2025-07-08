package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.*;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class VTPassUtilityServiceGateway implements UtilityServiceGateway {
    @Value("${vtpass.staticKey}")
    private String staticApiKey;

    @Value("${vtpass.secretKey}")
    private String apiKey;

    @Value("${vtpass.publicKey}")
    private String publicKey;

    @Value("${vtpass.baseUrl}")
    private String baseUrl;

    private static ZoneId LAGOS_ZONE = ZoneId.of("Africa/Lagos");
    private static DateTimeFormatter REQUEST_ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    public String generateRequestId() {
        // Step 1: Get current datetime in Africa/Lagos
        LocalDateTime nowInLagos = LocalDateTime.now(LAGOS_ZONE);

        // Step 2: Format to "YYYYMMDDHHmm"
        String timePart = nowInLagos.format(REQUEST_ID_FORMAT);

        // Step 3: Append random alphanumeric string (e.g., from UUID)
        String randomPart = UUID.randomUUID().toString().replace("-", "").substring(0, 10);

        // Final request_id
        return timePart + randomPart;
    }

    public NetworkContentDto getVariationCodes(String serviceId) {
        try {
            String url = baseUrl + "/service-variations?serviceID=" + serviceId;  // e.g. usdtngn

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
//                    .header("Authorization", "Your API Key")
//                    .header("accept", "application/json")
                    .header("api-key", staticApiKey)
                    .header("public-key", publicKey)
                    .GET()
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            var response = gson.fromJson(getResponse.body(), VariationCodeResponseWrapper.class);
            return response.getContent();
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to fetch VTPass variation codes..");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public VTPassPurchaseResponse purchaseProduct(VTPassPurchaseRequest request) {
        try {
            String url = baseUrl + "/service-variations?serviceID=" + request.getServiceID();  // e.g. usdtngn

            Gson gson = new Gson();
            String jsonRequest = gson.toJson(request);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("api-key", staticApiKey)
                    .header("secret-key", apiKey)
//                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            var response = gson.fromJson(postResponse.body(), VTPassPurchaseResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to purchase product on VTPass.");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public VTPassQueryTransactionResponse fetchTransactionStatus(String requestId) {
        try {
            String url = baseUrl + "/requery";  // e.g. usdtngn

            Gson gson = new Gson();

            var request = new VTPassQueryTransactionRequest(requestId);
            String jsonRequest = gson.toJson(request);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("api-key", staticApiKey)
                    .header("secret-key", apiKey)
//                    .header("accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            var response = gson.fromJson(postResponse.body(), VTPassQueryTransactionResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to verify transaction on VTPass.");
            System.out.println(ex.getMessage());
        }
        return null;
    }
}


