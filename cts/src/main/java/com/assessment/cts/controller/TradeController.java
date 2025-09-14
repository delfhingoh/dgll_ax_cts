package com.assessment.cts.controller;

import com.assessment.cts.model.TradeRequestDTO;
import com.assessment.cts.model.TradeResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @PostMapping("/buy")
    TradeResponseDTO buy(@RequestBody TradeRequestDTO tradeRequestDTO){
        return null;
    }

    @PostMapping("/sell")
    TradeResponseDTO sell(@RequestBody TradeRequestDTO tradeRequestDTO){
        return null;
    }
}
