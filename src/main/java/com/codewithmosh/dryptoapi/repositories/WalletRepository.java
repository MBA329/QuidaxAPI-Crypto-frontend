package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByCryptoCurrency(String cryptoCurrency);
    Optional<Wallet> findByCryptoCurrencyAndActiveTrue(String cryptoCurrency);
    List<Wallet> findAllByCryptoCurrencyAndActive(String cryptoCurrency, boolean active);
}
