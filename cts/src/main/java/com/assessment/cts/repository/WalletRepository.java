package com.assessment.cts.repository;

import com.assessment.cts.entity.User;
import com.assessment.cts.entity.Wallet;
import com.assessment.cts.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserAndWalletType(User user, ProductType walletType);
}
