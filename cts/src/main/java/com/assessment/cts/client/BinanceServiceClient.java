package com.assessment.cts.client;

import com.assessment.cts.model.api.binance.BinanceTickerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "binance-service-client", url = "${api.binance.url}")
public interface BinanceServiceClient {
    @GetMapping("/api/v3/ticker/bookTicker")
    BinanceTickerResponse getTickers();
}
