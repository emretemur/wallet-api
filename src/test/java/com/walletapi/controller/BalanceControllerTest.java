package com.walletapi.controller;

import com.walletapi.model.Balance;
import com.walletapi.model.enums.CurrencyEnum;
import com.walletapi.model.response.BalanceResponse;
import com.walletapi.service.BalanceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BalanceController.class)
public class BalanceControllerTest {

    @MockBean
    private BalanceService balanceService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetPlayerBalanceSuccessfully() throws Exception {
        final Long playerId = 123L;
        final List<Balance> balanceList = new ArrayList<>();
        balanceList.add(new Balance(BigDecimal.ZERO, CurrencyEnum.DOLLAR.name()));
        balanceList.add(new Balance(BigDecimal.ZERO, CurrencyEnum.EURO.name()));

        final BalanceResponse response = BalanceResponse.builder()
                .playerId(123L)
                .balance(balanceList)
                .build();
        doReturn(response).when(balanceService).getPlayerBalance(playerId);
        when(balanceService.getPlayerBalance(anyLong())).thenReturn(response);

        mockMvc.perform(get("/wallet/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("playerId", playerId))
                .andExpect(status().isOk());

        verify(balanceService).getPlayerBalance(anyLong());
    }
}
