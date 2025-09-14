package com.assessment.cts.repository;

import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(Status status);
}
