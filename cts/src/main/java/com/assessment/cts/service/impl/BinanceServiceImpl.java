package com.assessment.cts.service.impl;

import com.assessment.cts.client.BinanceServiceClient;
import com.assessment.cts.model.PriceResponse;
import com.assessment.cts.model.api.binance.BinanceTicker;
import com.assessment.cts.service.ExchangePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BinanceServiceImpl implements ExchangePriceService<BinanceTicker> {

    private final BinanceServiceClient client;

    @Override
    public List<PriceResponse> fetchPrices() {
        return List.of();
    }

    @Override
    public PriceResponse mapToPrice(BinanceTicker price) {
        return null;
    }
}
