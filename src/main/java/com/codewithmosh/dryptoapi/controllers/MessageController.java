package com.codewithmosh.dryptoapi.controllers;

import com.codewithmosh.dryptoapi.services.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@AllArgsConstructor
public class MessageController {
    private final WalletService walletService;
    @RequestMapping("/hello")
    public String sayHello() {
//        walletService.loadWallets();
//        walletService.createWallets();
        return "Hello, world!";
    }
}
