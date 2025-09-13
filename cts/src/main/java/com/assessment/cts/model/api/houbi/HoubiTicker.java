package com.assessment.cts.model.api.houbi;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class HoubiTicker {
    private String symbol;
    private BigDecimal bid;
    private BigDecimal bidSize;
    private BigDecimal ask;
    private BigDecimal askSize;
}
