package com.walletapi.model.request;

import com.walletapi.model.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryRequest {

    private String transactionId;
    private Long playerId;
    private BigDecimal balance;
    private BigDecimal transactionAmount;
    private CurrencyEnum currency;
    private String transactionType;
    private String description;
    private Integer direction;
}