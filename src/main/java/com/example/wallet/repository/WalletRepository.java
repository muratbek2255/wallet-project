package com.example.wallet.repository;

import com.example.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    @Query(value = "SELECT * FROM wallet.wallets WHERE wallet.wallets.id = ?1", nativeQuery = true)
    Wallet getById(@Param("id") int id);
}
