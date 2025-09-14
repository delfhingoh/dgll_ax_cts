package com.assessment.cts.model;

import com.assessment.cts.enums.ProductType;
import com.assessment.cts.enums.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBalanceResponseDTO {
    private String owner;
    private ProductType walletType;
    private String currency;
    private BigDecimal availableBalance;
    private Status status;
}
