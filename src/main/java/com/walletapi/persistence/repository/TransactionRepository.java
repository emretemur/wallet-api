package com.walletapi.persistence.repository;


import com.walletapi.persistence.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionEntity, String> {

    Optional<TransactionEntity> findByTransactionId(String transactionId);

    Page<TransactionEntity> findByPlayerIdAndCreatedDateBetweenOrderByCreatedDateDesc(Long playerId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}