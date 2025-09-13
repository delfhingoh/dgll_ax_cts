package com.assessment.cts.service;

import com.assessment.cts.model.PriceResponse;

import java.util.List;

public interface ExchangePriceService<T> {
    List<PriceResponse> fetchPrices();
    PriceResponse mapToPrice(T price);
}
