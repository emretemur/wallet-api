package com.walletapi.controller;

import com.walletapi.model.request.CreditRequest;
import com.walletapi.model.request.WithdrawRequest;
import com.walletapi.model.response.TransactionHistoryResponse;
import com.walletapi.model.response.TransactionResponse;
import com.walletapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/wallet/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(path = "/credit", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse creditMoney(@RequestHeader Long playerId, @Valid @RequestBody CreditRequest creditRequest) {
        return transactionService.creditMoney(playerId, creditRequest);
    }

    @PostMapping(path = "/withdraw", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionResponse withdrawMoney(@RequestHeader Long playerId, @Valid @RequestBody WithdrawRequest withdrawRequest) {
        return transactionService.withdrawMoney(playerId, withdrawRequest);
    }

    @GetMapping(path = "/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public TransactionHistoryResponse getTransactionHistory(@RequestHeader Long playerId,
                                                            @RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
                                                            @RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
                                                            @RequestParam int pageIndex, @RequestParam int pageSize) {
        return transactionService.transactionHistory(playerId, startDate, endDate, pageIndex, pageSize);
    }
}