package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.*;
import com.codewithmosh.dryptoapi.entities.TransactionStatus;
import com.codewithmosh.dryptoapi.entities.Wallet;
import com.codewithmosh.dryptoapi.exceptions.TransactionNotFoundException;
import com.codewithmosh.dryptoapi.repositories.TransactionRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class QuidaxCryptoPaymentGateway implements CryptoPaymentGateway {
    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    @Value("${quidax.baseUrl}")
    private String baseUrl;

    @Value("${quidax.secretKey}")
    private String apiKey;
    private final UtilityServiceGateway serviceGateway;

    public TickerResponse getBuyPrice(String marketPair) {
        try {
            String url = baseUrl + "/markets/tickers/" + marketPair.toLowerCase();  // e.g. usdtngn

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
//                    .header("Authorization", "Your API Key")
                    .header("accept", "application/json")
                    .GET()
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            var response = gson.fromJson(getResponse.body(), TickerResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to fetch Quidax buy price for market.");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public FetchWalletResponse fetchCryptoWallet(String cryptoCurrency) {
        try {
            String url = baseUrl + "/users/me/wallets/" + cryptoCurrency.toLowerCase() + "/address";  // e.g. usdtngn

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .GET()
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            var response = gson.fromJson(getResponse.body(), FetchWalletResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to fetch Quidax wallet details for wallet.");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public FetchWalletsResponse fetchPaymentAddresses(String cryptoCurrency) {
        try {
            String url = baseUrl + "/users/me/wallets/" + cryptoCurrency.toLowerCase() + "/addresses";  // e.g. usdtngn

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .GET()
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            var response = gson.fromJson(getResponse.body(), FetchWalletsResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to fetch Quidax wallet details for wallet.");
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @Override
    public FetchWalletResponse createPaymentAddress(String cryptoCurrency) {
        try {
            String url = baseUrl + "/users/me/wallets/" + cryptoCurrency.toLowerCase() + "/addresses";  // e.g. usdtngn
            var request = new CreatePaymentAddressRequest("me", cryptoCurrency.toLowerCase());
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(request);
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpResponse<String> getResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            var response = gson.fromJson(getResponse.body(), FetchWalletResponse.class);
            return response;
        }
        catch (Exception ex) {
            System.out.println("Error: Failed to create Quidax wallet for user.");
            System.out.println(ex.getMessage());
        }
        return null;
    }
    @Transactional
    @Override
    public void processWebhook(String rawPayload) {
        try {
            JsonObject root = JsonParser.parseString(rawPayload).getAsJsonObject();
            String event = root.has("event") ? root.get("event").getAsString() : null;

            if ("deposit.successful".equalsIgnoreCase(event)) {
                JsonObject data = root.getAsJsonObject("data");

                String id = data.has("id") ? data.get("id").getAsString() : null;
                String txid = data.has("txid") ? data.get("txid").getAsString() : null;
                String currency = data.has("currency") ? data.get("currency").getAsString() : null;
                BigDecimal amount = data.has("amount") ? data.get("amount").getAsBigDecimal() : BigDecimal.valueOf(0.0);

                String depositAddress = null;
                if (data.has("wallet")) {
                    JsonObject wallet = data.getAsJsonObject("wallet");
                    depositAddress = wallet.has("deposit_address") ? wallet.get("deposit_address").getAsString() : null;
                }
                var transaction = transactionRepository.findByWallet_DepositAddress(depositAddress).orElse(null);
                if (transaction == null) {
                    throw new TransactionNotFoundException();
                }

                if (transaction.getExpiresAt() != null && transaction.getExpiresAt().isBefore(LocalDateTime.now())) {
                    System.out.println("Transaction expired");
                    return;
                }

                if (amount.compareTo(transaction.getAmountCrypto()) >= 0) {
                    transaction.setTransactionStatus(TransactionStatus.PAID);
                    transaction.getWallet().setActive(false);
                    transaction.setTransactionId(id);
                    transaction.setTransactionHash(txid);
                    transactionRepository.save(transaction);

                    var response = serviceGateway.purchaseProduct(new VTPassPurchaseRequest(
                            transaction.getRequestId(),
                            transaction.getServiceId(),
                            transaction.getBillersCode(),
                            transaction.getDataPlanCode(),
                            transaction.getPhoneNumber()

                    ));
                    System.out.println("in progress");
                }
//                System.out.println("✅ Quidax Deposit Successful");
//                System.out.println("ID: " + id);
//                System.out.println("TXID: " + txid);
//                System.out.println("Currency: " + currency);
//                System.out.println("Amount: " + amount);
//                System.out.println("Deposit Address: " + depositAddress);

                // TODO: use id, txid, currency, amount, depositAddress to verify and credit user
            } else if ("wallet_address.generated".equalsIgnoreCase(event)) {
                JsonObject data = root.getAsJsonObject("data");

                String walletId = data.has("id") ? data.get("id").getAsString() : null;
                String currency = data.has("currency") ? data.get("currency").getAsString() : null;
                String network = data.has("network") ? data.get("network").getAsString() : null;
                String depositAddress = data.has("deposit_address") ? data.get("deposit_address").getAsString() : null;
                var wallet = new FetchWalletResponse();
                wallet.setId(walletId);
                wallet.setCryptoCurrency(currency);
                wallet.setDepositAddress(depositAddress);
                wallet.setNetwork(network);

                walletService.createWallet(wallet);
                String userId = null;
                String userEmail = null;

                if (data.has("user")) {
                    JsonObject user = data.getAsJsonObject("user");
                    userId = user.has("id") ? user.get("id").getAsString() : null;
                    userEmail = user.has("email") ? user.get("email").getAsString() : null;
                }

                System.out.println("✅ Wallet Address Generated");
                System.out.println("Wallet ID: " + walletId);
                System.out.println("Currency: " + currency);
                System.out.println("Network: " + network);
                System.out.println("Deposit Address: " + depositAddress);
                System.out.println("User ID: " + userId);
                System.out.println("User Email: " + userEmail);
            } else {
                System.out.println("Ignored event: " + event);
            }
        } catch (Exception ex) {
            System.out.println("❌ Error processing webhook:");
            System.out.println(ex.getMessage());
        }
    }

}
