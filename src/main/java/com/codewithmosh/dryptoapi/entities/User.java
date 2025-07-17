//package com.codewithmosh.dryptoapi.entities;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.LinkedHashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Entity
//@Getter
//@Setter
//@Table(name = "users")
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(name = "id")
//    private UUID id;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "password")
//    private String password;
//
//    @OneToMany(mappedBy = "user")
//    private Set<Wallet> wallets = new LinkedHashSet<>();
//}
