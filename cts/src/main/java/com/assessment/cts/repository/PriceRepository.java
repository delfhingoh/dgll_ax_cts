package com.assessment.cts.repository;

import com.assessment.cts.entity.Price;
import com.assessment.cts.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * For future plan: I think might be better to have LatestPrice Entity
 * */
@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    @EntityGraph(attributePaths = "product")
    Price findFirstByProductOrderByCreatedAtDesc(Product product);

    @Query(
        value = "SELECT * FROM (" +
                " SELECT *, ROW_NUMBER() OVER (PARTITION BY product_id ORDER BY created_at DESC) AS rn" +
                " FROM price" +
                ") sub WHERE rn = 1",
        nativeQuery = true
    )
    List<Price> findLatestPricePerProduct();
}
