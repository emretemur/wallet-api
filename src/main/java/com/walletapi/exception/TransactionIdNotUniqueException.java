package com.walletapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TransactionIdNotUniqueException extends ResponseStatusException {

    public TransactionIdNotUniqueException() {
        super(HttpStatus.BAD_REQUEST, "TransactionId is not valid!");
    }
}