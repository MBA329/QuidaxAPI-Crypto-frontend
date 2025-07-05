package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
