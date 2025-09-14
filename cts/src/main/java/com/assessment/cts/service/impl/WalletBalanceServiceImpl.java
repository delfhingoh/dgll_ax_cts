package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.entity.WalletBalance;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;
import com.assessment.cts.repository.WalletBalanceRepository;
import com.assessment.cts.service.HelperUtility;
import com.assessment.cts.service.WalletBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class WalletBalanceServiceImpl implements WalletBalanceService {

    private final WalletBalanceRepository walletBalanceRepository;
    private final HelperUtility helper;

    @Override
    public ResponseDTO<WalletBalance> findByWalletUserUuidAndCurrencyCode(String uuid, String currencyCode) {
        try {
            Optional<WalletBalance> walletBalanceOptional = this.walletBalanceRepository.findByWalletUserUuidAndCurrencyCode(uuid, currencyCode);
            if (walletBalanceOptional.isEmpty()) {
                return this.helper.transformToResponseDTO(null, ResponseStatus.INVALID, "No wallet balance found.");
            }
            return this.helper.transformToResponseDTO(walletBalanceOptional.get(), ResponseStatus.SUCCESS, "Retrieved wallet balance by uuid and currency code");
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseListDTO<WalletBalance> findByWalletUserUuid(String uuid) {
        try {
            List<WalletBalance> walletBalances = this.walletBalanceRepository.findByWalletUserUuid(uuid);
            if (walletBalances.isEmpty()) {
                return this.helper.transformToResponseListDTO(null, ResponseStatus.INVALID, "User have no wallets.");
            }
            return this.helper.transformToResponseListDTO(walletBalances, ResponseStatus.SUCCESS, "Retrieved wallet balance by uuid.");
        } catch (Exception e) {
            return this.helper.transformToResponseListDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseDTO<TradeResponseDTO> updateAllWalletBalances(List<WalletBalance> walletBalances) {
        try {
            this.walletBalanceRepository.saveAll(walletBalances);
            return this.helper.transformToResponseDTO(null, ResponseStatus.SUCCESS, "Update wallet balances successfully.");
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
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
