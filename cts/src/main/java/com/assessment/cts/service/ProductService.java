package com.assessment.cts.service;

import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;

import java.util.List;

public interface ProductService {
    Product getProductBySymbol(String symbol);
    List<Product> getProductsByStatus(Status status);
}
