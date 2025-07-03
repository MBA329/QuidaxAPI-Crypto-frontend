package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
