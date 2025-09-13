package com.assessment.cts.client;

import com.assessment.cts.model.api.houbi.HoubiTickerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "houbi-service-client", url = "${api.houbi.url}")
public interface HoubiServiceClient {
    @GetMapping("/market/tickers")
    HoubiTickerResponse getTickers();
}
