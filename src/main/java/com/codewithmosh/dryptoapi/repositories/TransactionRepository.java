package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
