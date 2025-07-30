package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.data.AppContext;
import com.codewithmosh.dryptoapi.dtos.PurchaseRequest;
import com.codewithmosh.dryptoapi.dtos.TickerResponse;
import com.codewithmosh.dryptoapi.services.CryptoPaymentGateway;
import com.codewithmosh.dryptoapi.services.PurchaseService;
import com.codewithmosh.dryptoapi.services.UtilityServiceGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CryptoPaymentGateway paymentGateway;

    @MockBean
    private UtilityServiceGateway serviceGateway;

    @MockBean
    private PurchaseService purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCryptoBuyPrice() throws Exception {
        // Given
        TickerResponse tickerResponse = new TickerResponse();
        tickerResponse.setBid(BigDecimal.valueOf(50000000));
        when(paymentGateway.getBuyPrice("btcngn")).thenReturn(tickerResponse);

        // When & Then
        mockMvc.perform(get("/purchase/buy-price/btcngn"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bid").value(50000000));
    }

    @Test
    void shouldReturnAvailableNetworks() throws Exception {
        mockMvc.perform(get("/purchase/networks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("airtel"))
                .andExpect(jsonPath("$[1]").value("mtn"))
                .andExpect(jsonPath("$[2]").value("glo"))
                .andExpect(jsonPath("$[3]").value("etisalat"));
    }

    @Test
    void shouldReturnAvailableCryptoCurrencies() throws Exception {
        mockMvc.perform(get("/purchase/crypto-currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("btc"))
                .andExpect(jsonPath("$[1]").value("usdt"))
                .andExpect(jsonPath("$[2]").value("trx"));
    }

    @Test
    void shouldHandleBuyDataRequest() throws Exception {
        // Given
        PurchaseRequest request = new PurchaseRequest();
        request.setPhoneNumber("08012345678");
        request.setNetwork("mtn");
        request.setCryptoCurrency("btc");
        request.setDataPlanCode("mtn-data-500mb");

        // When & Then
        mockMvc.perform(post("/purchase/buy-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFetchDataPlans() throws Exception {
        mockMvc.perform(post("/purchase/fetch-plans/mtn"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldVerifyPurchase() throws Exception {
        mockMvc.perform(post("/purchase/verify/test-request-id"))
                .andExpect(status().isOk());
    }
}
