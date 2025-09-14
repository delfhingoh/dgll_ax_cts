package com.assessment.cts.repository;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.entity.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {
}
