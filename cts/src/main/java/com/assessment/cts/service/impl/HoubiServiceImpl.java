package com.assessment.cts.service.impl;

import com.assessment.cts.client.HoubiServiceClient;
import com.assessment.cts.enums.PriceSource;
import com.assessment.cts.model.api.PriceResponse;
import com.assessment.cts.model.api.houbi.HoubiTicker;
import com.assessment.cts.model.api.houbi.HoubiTickerResponse;
import com.assessment.cts.service.ExchangePriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HoubiServiceImpl implements ExchangePriceService<HoubiTicker> {

    private final HoubiServiceClient client;

    @Override
    public List<PriceResponse> fetchPrices() {
        try {
            HoubiTickerResponse response = client.getTickers();
            return response.getData().stream()
                    .map(this::mapToPrice)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch houbi prices.", e);
            return List.of();
        }
    }

    @Override
    public PriceResponse mapToPrice(HoubiTicker price) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setSource(PriceSource.HOUBI);
        priceResponse.setSymbol(price.getSymbol().toUpperCase());
        priceResponse.setBidPrice(price.getBid());
        priceResponse.setAskPrice(price.getAsk());
        priceResponse.setBidQuantity(price.getBidSize());
        priceResponse.setAskQuantity(price.getAskSize());
        priceResponse.setUpdateAt(LocalDateTime.now());
        return priceResponse;
    }
}
