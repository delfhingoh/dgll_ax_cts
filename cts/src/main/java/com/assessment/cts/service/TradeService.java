package com.assessment.cts.service;

import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.TradeRequestDTO;
import com.assessment.cts.model.TradeResponseDTO;

public interface TradeService {
    ResponseDTO<TradeResponseDTO> buy(TradeRequestDTO tradeRequestDTO);
    ResponseDTO<TradeResponseDTO> sell(TradeRequestDTO tradeRequestDTO);
}
