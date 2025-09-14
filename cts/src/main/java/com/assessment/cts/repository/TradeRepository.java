package com.assessment.cts.repository;

import com.assessment.cts.entity.Trade;
import com.assessment.cts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    List<Trade> findByUserOrderByCreatedAt(User user);
}
