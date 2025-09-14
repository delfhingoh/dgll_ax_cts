package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;
import com.assessment.cts.repository.ProductRepository;
import com.assessment.cts.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Product getProductBySymbol(String symbol) {
        return productRepository.findBySymbol(symbol);
    }

    @Override
    public List<Product> getProductsByStatus(Status status) {
        return this.productRepository.findByStatus(status);
    }
}
