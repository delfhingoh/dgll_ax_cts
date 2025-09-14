package com.assessment.cts.model.initialise;

import com.assessment.cts.enums.ProductType;
import com.assessment.cts.enums.Status;
import lombok.Data;

@Data
public class WalletInit {
    private String userEmail;
    private ProductType walletType;
    private Status status;
}
