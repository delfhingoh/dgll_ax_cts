package com.assessment.cts.service.impl;

import com.assessment.cts.client.BinanceServiceClient;
import com.assessment.cts.enums.PriceSource;
import com.assessment.cts.model.PriceResponse;
import com.assessment.cts.model.api.binance.BinanceTicker;
import com.assessment.cts.service.ExchangePriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BinanceServiceImpl implements ExchangePriceService<BinanceTicker> {

    private final BinanceServiceClient client;

    @Override
    public List<PriceResponse> fetchPrices() {
        try {
            List<BinanceTicker> response = client.getTickers();
            return response.stream()
                    .map(this::mapToPrice)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch binance prices.", e);
            return List.of();
        }
    }

    @Override
    public PriceResponse mapToPrice(BinanceTicker price) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setSource(PriceSource.BINANCE);
        priceResponse.setSymbol(price.getSymbol());
        priceResponse.setBidPrice(price.getBidPrice());
        priceResponse.setAskPrice(price.getAskPrice());
        priceResponse.setBidQuantity(price.getBidQty());
        priceResponse.setAskQuantity(price.getAskQty());
        priceResponse.setUpdateAt(LocalDateTime.now());
        return priceResponse;
    }
}
