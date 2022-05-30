package com.walletapi.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.walletapi.model.enums.DirectionEnum;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {

    private Long playerId;
    private BigDecimal amount;
    private String currency;
    private String description;
    private String transactionId;
    private String transactionType;
    private DirectionEnum direction;
    private String transactionDate;
}