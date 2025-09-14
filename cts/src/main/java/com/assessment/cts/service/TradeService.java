package com.assessment.cts.service;

import com.assessment.cts.model.TradeRequestDTO;
import com.assessment.cts.model.TradeResponseDTO;

public interface TradeService {
    TradeResponseDTO buy(TradeRequestDTO tradeRequestDTO);
    TradeResponseDTO sell(TradeRequestDTO tradeRequestDTO);
}
