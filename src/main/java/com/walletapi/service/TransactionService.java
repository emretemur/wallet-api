package com.walletapi.service;

import com.walletapi.exception.AccountNotFoundException;
import com.walletapi.exception.DefaultException;
import com.walletapi.exception.InsufficientBalanceException;
import com.walletapi.exception.TransactionAmountNotValid;
import com.walletapi.exception.TransactionIdNotUniqueException;
import com.walletapi.model.enums.DirectionEnum;
import com.walletapi.model.enums.TransactionTypeEnum;
import com.walletapi.model.request.CreditRequest;
import com.walletapi.model.request.TransactionHistoryRequest;
import com.walletapi.model.request.WithdrawRequest;
import com.walletapi.model.response.TransactionHistoryResponse;
import com.walletapi.model.response.TransactionResponse;
import com.walletapi.persistence.entity.TransactionEntity;
import com.walletapi.persistence.repository.BalanceRepository;
import com.walletapi.persistence.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh:MM:ss");

    @Transactional
    public TransactionResponse creditMoney(Long playerId, CreditRequest creditRequest) {
        checkRequestedAmount(creditRequest.getAmount());
        // Balance table is locked to prevent multiple withdrawal and deposit requests at the same time.
        var balanceEntity = balanceRepository.findByPlayerIdAndCurrency(playerId, creditRequest.getCurrency().name()).orElseThrow(() -> new AccountNotFoundException());
        checkTransactionId(creditRequest.getTransactionId());

        balanceEntity.setBalance(balanceEntity.getBalance().add(creditRequest.getAmount()));
        balanceRepository.save(balanceEntity);

        saveTransactionHistory(TransactionHistoryRequest.builder()
                .playerId(playerId)
                .transactionId(creditRequest.getTransactionId())
                .transactionAmount(creditRequest.getAmount())
                .balance(balanceEntity.getBalance())
                .currency(creditRequest.getCurrency())
                .transactionType(TransactionTypeEnum.CREDIT.name())
                .description(DirectionEnum.IN.getDescription() + " " + creditRequest.getDescription())
                .direction(DirectionEnum.IN.getCode())
                .build());

        return TransactionResponse.builder()
                .amount(balanceEntity.getBalance())
                .currency(balanceEntity.getCurrency())
                .build();
    }

    @Transactional
    public TransactionResponse withdrawMoney(Long playerId, WithdrawRequest withdrawRequest) {
        checkRequestedAmount(withdrawRequest.getAmount());
        // The table is locked to prevent multiple withdrawal and deposit requests at the same time.
        var balanceEntity = balanceRepository.findByPlayerIdAndCurrency(playerId, withdrawRequest.getCurrency().name()).orElseThrow(() -> new AccountNotFoundException());
        checkTransactionId(withdrawRequest.getTransactionId());

        if (withdrawRequest.getAmount().compareTo(balanceEntity.getBalance()) > 0) {
            log.error("Insufficient balance!");
            throw new InsufficientBalanceException();
        }

        balanceEntity.setBalance(balanceEntity.getBalance().subtract(withdrawRequest.getAmount()));
        balanceRepository.save(balanceEntity);

        saveTransactionHistory(TransactionHistoryRequest.builder()
                .playerId(playerId)
                .transactionId(withdrawRequest.getTransactionId())
                .balance(balanceEntity.getBalance())
                .transactionAmount(withdrawRequest.getAmount())
                .transactionType(TransactionTypeEnum.WITHDRAW.name())
                .currency(withdrawRequest.getCurrency())
                .description(DirectionEnum.OUT.getDescription() + " " + withdrawRequest.getDescription())
                .direction(DirectionEnum.OUT.getCode())
                .build());

        return TransactionResponse.builder()
                .amount(balanceEntity.getBalance())
                .currency(balanceEntity.getCurrency())
                .build();
    }

    private void checkRequestedAmount(BigDecimal amount) {
        if (!(amount.compareTo(BigDecimal.ZERO) > 0)) {
            log.error("Requested amount is not valid");
            throw new TransactionAmountNotValid();
        }
    }

    public void checkTransactionId(String transactionId) {
        if (transactionRepository.findByTransactionId(transactionId).isPresent()) {
            log.error("TransactionId is not unique, {}", transactionId);
            throw new TransactionIdNotUniqueException();
        }
    }

    @Transactional
    public void saveTransactionHistory(TransactionHistoryRequest transactionHistoryRequest) {
        try {
            transactionRepository.save(TransactionEntity.builder()
                    .playerId(transactionHistoryRequest.getPlayerId())
                    .transactionId(transactionHistoryRequest.getTransactionId())
                    .balance(transactionHistoryRequest.getBalance())
                    .currency(transactionHistoryRequest.getCurrency().name())
                    .transactionAmount(transactionHistoryRequest.getTransactionAmount())
                    .transactionType(transactionHistoryRequest.getTransactionType())
                    .description(transactionHistoryRequest.getDescription())
                    .direction(transactionHistoryRequest.getDirection())
                    .build());
        } catch (Exception e) {
            log.error("Failed to save transaction, {}", transactionHistoryRequest.getTransactionId());
            throw new DefaultException();
        }
    }

    public TransactionHistoryResponse transactionHistory(Long playerId, LocalDateTime startDate, LocalDateTime endDate, int pageIndex, int pageSize) {
        var transactionEntityPage = transactionRepository.findByPlayerIdAndCreatedDateBetweenOrderByCreatedDateDesc(playerId, startDate, endDate, PageRequest.of(pageIndex, pageSize));

        return TransactionHistoryResponse.builder()
                .transactions(transactionEntityPage.getContent().stream().map(this::buildTransactionResponse).collect(Collectors.toList()))
                .totalElements(transactionEntityPage.getTotalElements())
                .totalPages(transactionEntityPage.getTotalPages())
                .build();
    }

    private TransactionResponse buildTransactionResponse(TransactionEntity transactionEntity) {
        return TransactionResponse.builder()
                .playerId(transactionEntity.getPlayerId())
                .transactionId(transactionEntity.getTransactionId())
                .amount(transactionEntity.getTransactionAmount())
                .currency(transactionEntity.getCurrency())
                .direction(DirectionEnum.of(transactionEntity.getDirection()))
                .transactionType(transactionEntity.getTransactionType())
                .description(transactionEntity.getDescription())
                .transactionDate(transactionEntity.getCreatedDate().format(formatter))
                .build();
    }
}
