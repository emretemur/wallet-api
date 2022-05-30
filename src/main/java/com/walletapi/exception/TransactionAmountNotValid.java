package com.walletapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TransactionAmountNotValid extends ResponseStatusException {

    public TransactionAmountNotValid() {
        super(HttpStatus.BAD_REQUEST, "Transaction amount is not valid!");
    }
}