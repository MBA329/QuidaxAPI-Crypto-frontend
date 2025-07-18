package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    Optional<Transaction> findByWallet_DepositAddress(String depositAddress);
    Optional<Transaction> findByRequestId(String requestId);
}
