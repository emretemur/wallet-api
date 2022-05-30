package com.walletapi.controller;

import com.walletapi.model.response.BalanceResponse;
import com.walletapi.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/wallet")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping(value="/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public BalanceResponse getPlayerBalance(@RequestHeader Long playerId)  {
        return balanceService.getPlayerBalance(playerId);
    }
}
