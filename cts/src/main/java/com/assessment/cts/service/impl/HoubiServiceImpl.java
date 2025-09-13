package com.assessment.cts.service.impl;

import com.assessment.cts.client.HoubiServiceClient;
import com.assessment.cts.model.PriceResponse;
import com.assessment.cts.model.api.houbi.HoubiTicker;
import com.assessment.cts.service.ExchangePriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HoubiServiceImpl implements ExchangePriceService<HoubiTicker> {

    private final HoubiServiceClient client;

    @Override
    public List<PriceResponse> fetchPrices() {
        return List.of();
    }

    @Override
    public PriceResponse mapToPrice(HoubiTicker price) {
        return null;
    }
}
