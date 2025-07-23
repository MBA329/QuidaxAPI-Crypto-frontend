package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Transaction;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.wallet.depositAddress = :address and t.isTerminated = false")
    Optional<Transaction> findByWalletDepositAddress(@Param("address") String depositAddress);
    Optional<Transaction> findByRequestId(String requestId);

    @Modifying
    @Transactional
    @Query("""
    UPDATE Transaction t
    SET t.isTerminated = true
    WHERE t.expiresAt < :cutoff
""")
    int terminateExpiredTransactions(@Param("cutoff") LocalDateTime cutoff);

}
