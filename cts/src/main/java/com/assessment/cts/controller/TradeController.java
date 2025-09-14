package com.assessment.cts.controller;

import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.TradeRequestDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.service.TradeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/trade")
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/buy")
    ResponseDTO<TradeResponseDTO> buy(@RequestBody TradeRequestDTO tradeRequestDTO){
        return this.tradeService.buy(tradeRequestDTO);
    }

    @PostMapping("/sell")
    ResponseDTO<TradeResponseDTO> sell(@RequestBody TradeRequestDTO tradeRequestDTO){
        return this.tradeService.sell(tradeRequestDTO);
    }
}
