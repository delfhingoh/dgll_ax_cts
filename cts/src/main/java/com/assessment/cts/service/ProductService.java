package com.assessment.cts.service;

import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;

import java.util.List;

public interface ProductService {
    List<Product> getProductsByStatus(Status status);
}
