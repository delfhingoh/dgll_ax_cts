package com.assessment.cts.model;

import com.assessment.cts.enums.TradeDirection;
import com.assessment.cts.enums.TradeStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TradeResponseDTO {
    private String symbol;
    private BigDecimal notionalAmount;
    private BigDecimal rateAmount;
    private BigDecimal finalAmount;
    private TradeStatus tradeStatus;
    private TradeDirection tradeDirection;
    private LocalDateTime tradeDateTime;
}
