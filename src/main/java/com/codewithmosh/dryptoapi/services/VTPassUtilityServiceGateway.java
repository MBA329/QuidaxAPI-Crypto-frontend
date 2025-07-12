package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    @Override
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
    @Override
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
    @Override
    public VTPassPurchaseResponse purchaseProduct(VTPassPurchaseRequest request) {
        try {
            String url = baseUrl + "/pay";

            Gson gson = new Gson();
            String jsonRequest = gson.toJson(request);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
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
    @Override
    public VTPassQueryTransactionResponse fetchTransactionStatus(String requestId) {
        try {
            String url = baseUrl + "/requery";  // e.g. usdtngn

            Gson gson = new Gson();

            var request = new VTPassQueryTransactionRequest(requestId);
            String jsonRequest = gson.toJson(request);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
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

    @Override
    public void processWebhook(String rawPayload) {
        try {
            JsonObject root = JsonParser.parseString(rawPayload).getAsJsonObject();

            String type = root.has("type") ? root.get("type").getAsString() : null;

            if (!"transaction-update".equalsIgnoreCase(type)) {
                System.out.println("Ignored webhook type: " + type);
                return;
            }

            JsonObject content = root.getAsJsonObject("content");

            String status = content.has("status") ? content.get("status").getAsString() : null;
            String requestId = content.has("request_id") ? content.get("request_id").getAsString() : null;
            String phone = content.has("phone") ? content.get("phone").getAsString() : null;
            String amount = content.has("amount") ? content.get("amount").getAsString() : null;
            String transactionId = content.has("transactionId") ? content.get("transactionId").getAsString() : null;

            System.out.println("✅ VTPass Transaction Update Received");
            System.out.println("Request ID: " + requestId);
            System.out.println("Status: " + status);
            System.out.println("Phone: " + phone);
            System.out.println("Amount: " + amount);
            System.out.println("Transaction ID: " + transactionId);

            // TODO: Use requestId to find matching local transaction and update its status in your DB

        } catch (Exception ex) {
            System.out.println("❌ Error processing VTPass webhook:");
            System.out.println(ex.getMessage());
        }
    }
}


