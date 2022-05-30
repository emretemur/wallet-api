package com.walletapi.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryResponse {

    private List<TransactionResponse> transactions;
    private long totalElements = 0;
    private int totalPages = 0;
}