package com.assessment.cts.service;

import com.assessment.cts.entity.User;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.enums.ProductType;

public interface WalletService {
    Wallet getOrCreateWallet(User user, ProductType walletType);
}
