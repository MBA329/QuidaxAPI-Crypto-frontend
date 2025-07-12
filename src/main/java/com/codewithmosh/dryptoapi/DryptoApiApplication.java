package com.codewithmosh.dryptoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DryptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DryptoApiApplication.class, args);

//        var user = context.getBean(User.class);

        System.out.println("working now");
    }

}
