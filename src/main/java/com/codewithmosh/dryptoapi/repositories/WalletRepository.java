package com.codewithmosh.dryptoapi.repositories;

import com.codewithmosh.dryptoapi.entities.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, String> {

    Optional<Wallet> findByCryptoCurrency(String cryptoCurrency);
    Optional<Wallet> findByCryptoCurrencyAndIsActiveFalse(String cryptoCurrency);
//    List<Wallet> findAllByCryptoCurrencyAndActive(String cryptoCurrency, boolean active);

    @Modifying
    @Transactional
    @Query("""
        UPDATE Wallet w
        SET w.isActive = false
        WHERE w.id IN (
            SELECT t.wallet.id FROM Transaction t
            WHERE t.expiresAt < :cutoff
        )
    """)
    int deactivateWalletsWithExpiredTransactions(LocalDateTime cutoff);
}
