package com.assessment.cts.model;

import com.assessment.cts.enums.ResponseStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceDTO {
    private String symbol;
    private BigDecimal bidPrice;
    private BigDecimal bidQuantity;
    private BigDecimal askPrice;
    private BigDecimal askQuantity;
    private LocalDateTime updatedOn;
    private ResponseStatus status;
    private String message;
}
