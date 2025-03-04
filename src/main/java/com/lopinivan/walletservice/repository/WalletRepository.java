package com.lopinivan.walletservice.repository;

import com.lopinivan.walletservice.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query(value = "SELECT pg_advisory_xact_lock(:lockKey)", nativeQuery = true)
    void executeNativeQuery(@Param("lockKey") long lockKey);
}
