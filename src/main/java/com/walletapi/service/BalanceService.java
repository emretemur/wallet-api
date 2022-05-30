package com.walletapi.service;

import com.walletapi.exception.AccountNotFoundException;
import com.walletapi.model.Balance;
import com.walletapi.model.response.BalanceResponse;
import com.walletapi.persistence.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public BalanceResponse getPlayerBalance(Long playerId) {
        var balanceEntities = balanceRepository.findByPlayerId(playerId);
        final List<Balance> balanceResponse = new ArrayList<>();
        if (!CollectionUtils.isEmpty(balanceEntities)) {
            balanceEntities.forEach(balance -> balanceResponse.add(Balance.builder()
                    .amount(balance.getBalance())
                    .currencyType(balance.getCurrency())
                    .build()));
            return BalanceResponse.builder().balance(balanceResponse).playerId(playerId).build();
        }
        throw new AccountNotFoundException();
    }
}