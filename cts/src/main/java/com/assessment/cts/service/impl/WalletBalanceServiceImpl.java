package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.entity.WalletBalance;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.repository.WalletBalanceRepository;
import com.assessment.cts.service.HelperUtility;
import com.assessment.cts.service.WalletBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Service
public class WalletBalanceServiceImpl implements WalletBalanceService {

    private final WalletBalanceRepository walletBalanceRepository;
    private HelperUtility helperUtility;

    @Override
    public ResponseDTO<TradeResponseDTO> updateAllWalletBalances(List<WalletBalance> walletBalances) {
        try {
            this.walletBalanceRepository.saveAll(walletBalances);
            return this.helperUtility.transformToResponseDTO(null, ResponseStatus.SUCCESS, "Update wallet balances successfully.");
        } catch (Exception e) {
            return this.helperUtility.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public WalletBalance getOrCreateWalletBalance(Wallet wallet, Currency currency) {
        return this.walletBalanceRepository
            .findByWalletAndCurrency(wallet, currency)
            .orElseGet(() -> {
                WalletBalance walletBalance = new WalletBalance();
                walletBalance.setWallet(wallet);
                walletBalance.setCurrency(currency);
                walletBalance.setAvailableBalance(BigDecimal.ZERO);
                return this.walletBalanceRepository.save(walletBalance);
        });
    }
}
