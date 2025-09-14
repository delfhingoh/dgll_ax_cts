package com.assessment.cts.repository;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.entity.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {
    Optional<WalletBalance> findByWalletAndCurrency(Wallet wallet, Currency currency);
    Optional<WalletBalance> findByWalletUserUuidAndCurrencyCode(String uuid, String currencyCode);
    List<WalletBalance> findByWalletUserUuid(String uuid);
}
