package com.walletapi.service;

import com.walletapi.exception.AccountNotFoundException;
import com.walletapi.exception.InsufficientBalanceException;
import com.walletapi.exception.TransactionIdNotUniqueException;
import com.walletapi.model.Balance;
import com.walletapi.model.enums.CurrencyEnum;
import com.walletapi.model.enums.DirectionEnum;
import com.walletapi.model.enums.TransactionTypeEnum;
import com.walletapi.model.request.CreditRequest;
import com.walletapi.model.request.TransactionHistoryRequest;
import com.walletapi.model.request.WithdrawRequest;
import com.walletapi.model.response.TransactionHistoryResponse;
import com.walletapi.model.response.TransactionResponse;
import com.walletapi.persistence.entity.BalanceEntity;
import com.walletapi.persistence.entity.TransactionEntity;
import com.walletapi.persistence.repository.BalanceRepository;
import com.walletapi.persistence.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.links.Link;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Spy
    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void shouldSuccessfullyCreditMoney() {
        final Long playerId = 123L;
        final CreditRequest creditRequest = CreditRequest.builder()
                .transactionId("1AB-2AB")
                .amount(BigDecimal.valueOf(20))
                .currency(CurrencyEnum.DOLLAR)
                .description("test")
                .channel("WEB")
                .build();
        final Optional<BalanceEntity> balanceEntity = Optional.of(BalanceEntity.builder().balance(BigDecimal.valueOf(100)).currency("DOLAR").build());

        doReturn(balanceEntity).when(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
        doNothing().when(transactionService).saveTransactionHistory(any(TransactionHistoryRequest.class));

        final TransactionResponse transactionResponse = transactionService.creditMoney(playerId, creditRequest);

        verify(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
        verify(transactionService).saveTransactionHistory(any(TransactionHistoryRequest.class));
        assertEquals(transactionResponse.getAmount(), BigDecimal.valueOf(120));
    }

    @Test
    public void shouldSuccessfullyWithdrawMoney() {
        final Long playerId = 123L;
        final WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .transactionId("2AB-3AB")
                .amount(BigDecimal.valueOf(20))
                .currency(CurrencyEnum.DOLLAR)
                .description("test")
                .channel("WEB")
                .build();
        final Optional<BalanceEntity> balanceEntity = Optional.of(BalanceEntity.builder().balance(BigDecimal.valueOf(100)).currency("DOLAR").build());

        doReturn(balanceEntity).when(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
        doNothing().when(transactionService).saveTransactionHistory(any(TransactionHistoryRequest.class));

        final TransactionResponse transactionResponse = transactionService.withdrawMoney(playerId, withdrawRequest);

        verify(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
        verify(transactionService).saveTransactionHistory(any(TransactionHistoryRequest.class));
        assertEquals(transactionResponse.getAmount(), BigDecimal.valueOf(80));
    }

    @Test
    public void shouldSuccessfullyGetTransactionHistory()  {
        final List<TransactionEntity> transactionEntities = Stream.of(
                TransactionEntity.builder()
                        .id("0000-0001")
                        .transactionAmount(BigDecimal.valueOf(20))
                        .transactionType(TransactionTypeEnum.WITHDRAW.name())
                        .transactionId("0ABC-1DEF")
                        .createdDate(LocalDateTime.now().minusDays(21))
                        .direction(DirectionEnum.OUT.getCode()).build(),
                TransactionEntity.builder()
                        .id("0000-0002")
                        .transactionAmount(BigDecimal.valueOf(50))
                        .transactionType(TransactionTypeEnum.CREDIT.name())
                        .transactionId("0ABC-1DEC")
                        .createdDate(LocalDateTime.now().minusDays(20))
                        .direction(DirectionEnum.IN.getCode()).build()).collect(Collectors.toList());

        Page<TransactionEntity> pages = new PageImpl<>(transactionEntities, Pageable.ofSize(2), transactionEntities.size());
        when(transactionRepository.findByPlayerIdAndCreatedDateBetweenOrderByCreatedDateDesc(anyLong(), any(), any(), any()))
                .thenReturn(pages);

        final TransactionHistoryResponse transactionHistoryResponse = transactionService.transactionHistory(123L, LocalDateTime.now().minusMonths(2), LocalDateTime.now(), 0, 20);

        assertEquals(2, transactionHistoryResponse.getTotalElements());
        assertEquals(transactionEntities.get(0).getTransactionId(), transactionHistoryResponse.getTransactions().get(0).getTransactionId());
    }

    @Test(expected = InsufficientBalanceException.class)
    public void shouldThrowInsufficientBalanceException() {
        final Long playerId = 1L;
        final WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .transactionId("0000-0001")
                .amount(BigDecimal.valueOf(60))
                .description("test")
                .currency(CurrencyEnum.DOLLAR)
                .build();
        final Optional<BalanceEntity> balanceEntityOptional = Optional.of(BalanceEntity.builder()
                .balance(BigDecimal.valueOf(50))
                .build());

        doReturn(balanceEntityOptional).when(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());

        transactionService.withdrawMoney(playerId, withdrawRequest);

        verify(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
    }

    @Test(expected = TransactionIdNotUniqueException.class)
    public void shouldThrowInvalidTransactionIdException() {
        final Long playerId = 1L;
        final CreditRequest creditRequest = CreditRequest.builder()
                .transactionId("0000-0001")
                .amount(BigDecimal.valueOf(70))
                .description("test")
                .currency(CurrencyEnum.DOLLAR)
                .build();
        final Optional<TransactionEntity> optionalTransactionEntity = Optional.of(TransactionEntity.builder()
                .transactionId("0000-0001")
                .build());
        final Optional<BalanceEntity> balanceEntityOptional = Optional.of(BalanceEntity.builder()
                .balance(BigDecimal.valueOf(50))
                .playerId(1L)
                .build());

        doReturn(balanceEntityOptional).when(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
        doReturn(optionalTransactionEntity).when(transactionRepository).findByTransactionId(any());

        transactionService.creditMoney(playerId, creditRequest);

        verify(transactionRepository).findByTransactionId(any());
    }


    @Test(expected = AccountNotFoundException.class)
    public void shouldThrowAccountNotFoundException() {
        final Long playerId = 123L;
        final CreditRequest creditRequest = CreditRequest.builder()
                .transactionId("0000-1111")
                .amount(BigDecimal.valueOf(100))
                .currency(CurrencyEnum.DOLLAR)
                .description("test")
                .build();

        doReturn(Optional.empty()).when(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());

        transactionService.creditMoney(playerId, creditRequest);

        verify(balanceRepository).findByPlayerIdAndCurrency(anyLong(), any());
    }
}


