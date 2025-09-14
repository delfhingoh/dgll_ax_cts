package com.assessment.cts.service.impl;

import com.assessment.cts.entity.User;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.enums.ProductType;
import com.assessment.cts.repository.WalletRepository;
import com.assessment.cts.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Override
    public Wallet getOrCreateWallet(User user, ProductType walletType) {
        return this.walletRepository
                .findByUserAndWalletType(user, walletType)
                .orElseGet(() -> {
                    Wallet wallet = new Wallet();
                    wallet.setUser(user);
                    wallet.setWalletType(walletType);
                    wallet.setWalletType(walletType);
                    return this.walletRepository.save(wallet);
        });
    }
}
