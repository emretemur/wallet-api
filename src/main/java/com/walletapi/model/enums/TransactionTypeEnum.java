package com.walletapi.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TransactionTypeEnum {
    CREDIT(10),
    WITHDRAW(20);

    private int code;
}