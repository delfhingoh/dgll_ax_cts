package com.assessment.cts.model.api.binance;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceTicker {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQty;
    private BigDecimal askPrice;
    private BigDecimal askQty;
}
