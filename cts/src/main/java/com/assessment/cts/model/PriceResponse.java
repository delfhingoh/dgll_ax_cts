package com.assessment.cts.model;

import com.assessment.cts.enums.PriceSource;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceResponse {
    private PriceSource source;
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQuantity;
    private BigDecimal askPrice;
    private BigDecimal askQuantity;
    private LocalDateTime updateAt;
}
