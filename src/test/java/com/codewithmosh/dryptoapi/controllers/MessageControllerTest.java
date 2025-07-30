package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.services.WalletService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WalletService walletService;

    @Test
    void shouldReturnHelloMessage() throws Exception {
        mockMvc.perform(get("/message/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world!"));
    }
}
