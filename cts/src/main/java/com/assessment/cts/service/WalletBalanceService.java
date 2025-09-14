package com.assessment.cts.service;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.entity.WalletBalance;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.model.TradeResponseDTO;

import java.util.List;

public interface WalletBalanceService {
    ResponseDTO<WalletBalance> findByWalletUserUuidAndCurrencyCode(String uuid, String currencyCode);
    ResponseListDTO<WalletBalance> findByWalletUserUuid(String uuid);
    ResponseDTO<TradeResponseDTO> updateAllWalletBalances(List<WalletBalance> walletBalances);
    WalletBalance getOrCreateWalletBalance(Wallet wallet, Currency currency);
}
