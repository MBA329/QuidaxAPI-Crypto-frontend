package com.codewithmosh.dryptoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DryptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DryptoApiApplication.class, args);

//        var user = context.getBean(User.class);
        System.out.println("working now");
    }

}
