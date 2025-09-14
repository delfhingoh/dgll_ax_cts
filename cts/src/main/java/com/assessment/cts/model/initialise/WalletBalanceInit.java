package com.assessment.cts.model.initialise;

import com.assessment.cts.enums.ProductType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletBalanceInit {
    private String userEmail;
    private String currencyCode;
    private ProductType walletType;
    private BigDecimal availableBalance;
}
