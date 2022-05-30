package com.walletapi.persistence.repository;

import com.walletapi.persistence.entity.BalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface BalanceRepository extends JpaRepository<BalanceEntity, Long> {

    List<BalanceEntity> findByPlayerId(Long playerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BalanceEntity> findByPlayerIdAndCurrency(Long playerId, String currency);
}