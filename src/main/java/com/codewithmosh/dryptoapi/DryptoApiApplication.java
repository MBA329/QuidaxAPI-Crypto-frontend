package com.codewithmosh.dryptoapi;

import com.codewithmosh.dryptoapi.entities.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DryptoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DryptoApiApplication.class, args);

//        var user = context.getBean(User.class);
        System.out.println("working now");
    }

}
