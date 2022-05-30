package com.walletapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletapi.model.enums.CurrencyEnum;
import com.walletapi.model.enums.DirectionEnum;
import com.walletapi.model.request.CreditRequest;
import com.walletapi.model.request.WithdrawRequest;
import com.walletapi.model.response.TransactionHistoryResponse;
import com.walletapi.model.response.TransactionResponse;
import com.walletapi.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
    @MockBean
    private TransactionService transactionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldCreditMoneySuccessfully() throws Exception {
        final Long playerId = 123L;
        final TransactionResponse response = TransactionResponse.builder().amount(BigDecimal.valueOf(123)).currency("DOLAR").build();

        final CreditRequest request = CreditRequest.builder()
                .transactionId("B1a")
                .amount(BigDecimal.valueOf(10L))
                .currency(CurrencyEnum.DOLLAR)
                .channel("iOS")
                .build();

        doReturn(response).when(transactionService).creditMoney(playerId, CreditRequest.builder().build());
        when(transactionService.creditMoney(anyLong(), any())).thenReturn(response);

        mockMvc.perform(post("/wallet/transaction/credit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("playerId", playerId)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(transactionService).creditMoney(anyLong(), any());
    }

    @Test
    public void shouldWithdrawMoneySuccessfully() throws Exception {
        final Long playerId = 123L;
        final TransactionResponse response = TransactionResponse.builder().amount(BigDecimal.valueOf(123)).currency(CurrencyEnum.DOLLAR.name()).build();

        final WithdrawRequest request = WithdrawRequest.builder()
                .transactionId("B1a")
                .amount(BigDecimal.valueOf(10L))
                .currency(CurrencyEnum.DOLLAR)
                .channel("iOS")
                .build();

        doReturn(response).when(transactionService).withdrawMoney(playerId, request);
        when(transactionService.withdrawMoney(anyLong(), any())).thenReturn(response);

        mockMvc.perform(post("/wallet/transaction/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("playerId", playerId)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(transactionService).withdrawMoney(anyLong(), any());
    }

    @Test
    public void shouldGetTransactionHistorySuccessfully() throws Exception {
        final Long playerId = 123L;
        List<TransactionResponse> transactionResponses = new ArrayList<>();
        final TransactionResponse transactionResponse1 = TransactionResponse.builder()
                .transactionDate("14.05.2022 12:00:13")
                .currency(CurrencyEnum.DOLLAR.name())
                .direction(DirectionEnum.IN)
                .transactionType("CREDİT")
                .transactionId("AB1")
                .amount(BigDecimal.valueOf(200))
                .build();
        final TransactionResponse transactionResponse2 = TransactionResponse.builder()
                .transactionDate("15.05.2022 12:00:13")
                .currency(CurrencyEnum.DOLLAR.name())
                .direction(DirectionEnum.IN)
                .transactionType("CREDİT")
                .transactionId("AB2")
                .amount(BigDecimal.valueOf(40))
                .build();
        transactionResponses.add(transactionResponse1);
        transactionResponses.add(transactionResponse2);
        final TransactionHistoryResponse response = TransactionHistoryResponse.builder()
                .transactions(transactionResponses)
                .totalElements(2)
                .totalPages(1)
                .build();

        when(transactionService.transactionHistory(anyLong(), any(), any(), anyInt(), anyInt())).thenReturn(response);

        final MvcResult mvcResult = mockMvc.perform(get("/wallet/transaction/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("playerId", playerId)
                        .param("startDate", "2022-03-04 00:00:00")
                        .param("endDate", "2022-06-04 00:00:00")
                        .param("pageIndex", "0")
                        .param("pageSize", "20"))
                .andExpect(status().isOk()).andReturn();

      final TransactionHistoryResponse historyResponse = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<TransactionHistoryResponse>() {
        });

        verify(transactionService).transactionHistory(anyLong(), any(), any(), anyInt(), anyInt());

        assertEquals(transactionResponses.size(), historyResponse.getTransactions().size());
    }
}