package com.walletapi.model.request;

import com.walletapi.model.enums.CurrencyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditRequest {

    private BigDecimal amount;
    @NotNull
    private CurrencyEnum currency;
    @NotBlank
    private String transactionId;
    private String channel;
    private String description;
}