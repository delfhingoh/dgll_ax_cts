package com.assessment.cts.service;

import com.assessment.cts.model.PriceResponse;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class PriceScheduler {

    private final List<ExchangePriceService<?>> exchangePriceServices;
    private final PriceAggregatorService priceAggregatorService;
    private final ExecutorService executorService;

    private final int SECONDS = 10000; // 10 seconds

    public PriceScheduler(List<ExchangePriceService<?>> exchangePriceServices, PriceAggregatorService priceAggregatorService) {
        this.exchangePriceServices = exchangePriceServices;
        this.priceAggregatorService = priceAggregatorService;

        // Pool size based on the number of implemented ExchangePriceService else 2
        int poolSize = Math.max(exchangePriceServices.size(), 2);
        this.executorService = Executors.newFixedThreadPool(poolSize);
        log.info("Initialized PriceScheduler with {} exchange price sources.", exchangePriceServices.size());
    }

    // Fetch prices from multiple sources concurrently then pass it to PriceAggregatorService
    @Scheduled(fixedRate = SECONDS)
    private void fetchPrices() {
        List<Future<List<PriceResponse>>> futures = new ArrayList<>();
        for (ExchangePriceService<?> exchangePriceService : exchangePriceServices) {
            futures.add(executorService.submit(exchangePriceService::fetchPrices));
        }

        List<PriceResponse> priceResponses = new ArrayList<>();
        for (Future<List<PriceResponse>> future : futures) {
            try {
                priceResponses.addAll(future.get());
            } catch (Exception e) {
                log.error("Error fetching prices from service.", e);
            }
        }

        priceAggregatorService.aggregatePrices(priceResponses);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
