package com.assessment.cts.model;

import com.assessment.cts.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRequestDTO {
    private String uuid;
    private String baseCurrency;
    private String quoteCurrency;
    private ProductType productType;
    private BigDecimal amount;
}
