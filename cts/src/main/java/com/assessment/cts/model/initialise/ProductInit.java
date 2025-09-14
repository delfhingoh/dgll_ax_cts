package com.assessment.cts.model.initialise;

import com.assessment.cts.enums.Status;
import lombok.Data;

@Data
public class ProductInit {
    private String baseCurrencyCode;
    private String quoteCurrencyCode;
    private String symbol;
    private Status status;
}
