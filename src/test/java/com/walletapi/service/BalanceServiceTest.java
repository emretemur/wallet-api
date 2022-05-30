package com.walletapi.service;

import com.walletapi.exception.AccountNotFoundException;
import com.walletapi.model.response.BalanceResponse;
import com.walletapi.persistence.entity.BalanceEntity;
import com.walletapi.persistence.repository.BalanceRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BalanceServiceTest {
    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    @Test
    public void shouldGetBalanceSuccessfully() {
        final Long playerId = 123L;
        final BalanceEntity balanceEntity1 = BalanceEntity.builder().balance(BigDecimal.valueOf(100)).currency("DOLAR").build();
        final BalanceEntity balanceEntity2 = BalanceEntity.builder().balance(BigDecimal.valueOf(200)).currency("EURO").build();
        final List<BalanceEntity> balanceEntities = new ArrayList<>();
        balanceEntities.add(balanceEntity1);
        balanceEntities.add(balanceEntity2);

        doReturn(balanceEntities).when(balanceRepository).findByPlayerId(anyLong());
        final BalanceResponse balanceResponse = balanceService.getPlayerBalance(playerId);

        assertEquals(balanceResponse.getBalance().get(0).getAmount(), BigDecimal.valueOf(100));
        assertEquals(balanceResponse.getBalance().get(1).getAmount(), BigDecimal.valueOf(200));
        assertEquals(balanceResponse.getPlayerId(), playerId);
    }

    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowAccountNotFoundException() {
        final Long playerId = 123L;

        doReturn(Lists.emptyList()).when(balanceRepository).findByPlayerId(anyLong());

        balanceService.getPlayerBalance(playerId);

        verify(balanceRepository).findByPlayerId(anyLong());
    }
}
