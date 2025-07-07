package com.codewithmosh.dryptoapi.services;

import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.dtos.FetchWalletResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class QuidaxCryptoPaymentGateway implements CryptoPaymentGateway {
    private final RestTemplate restTemplate;

    @Value("${quidax.baseUrl}")
    private String baseUrl;

    @Value("${quidax.secretKey}")
    private String apiKey;

    public TickerResponse getBuyPrice(String marketPair) {
        try {
            String url = baseUrl + "/markets/tickers/" + marketPair.toLowerCase();  // e.g. usdtngn

            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(new URI(url))
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




}

//String url = "http://api.assemblyai.com/v2/transcript/" + transcript.getId();
//
//HttpRequest getRequest = HttpRequest.newBuilder()
//        .uri(new URI(url))
//        .header("Authorization", "Your API Key")
//        .GET()
//        .build();
//
//HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());