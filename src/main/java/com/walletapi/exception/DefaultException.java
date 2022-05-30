package com.walletapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DefaultException extends ResponseStatusException {

    public DefaultException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong.");
    }
}