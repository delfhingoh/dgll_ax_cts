package com.assessment.cts.service;

import com.assessment.cts.model.api.PriceResponse;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class PriceScheduler {

    private final List<ExchangePriceService<?>> exchangePriceServices;
    private final PriceAggregatorService priceAggregatorService;
    private final ExecutorService executorService;

    private final long FETCH_PRICE_INTERVAL_MS = 10_000L; // 10 seconds

    public PriceScheduler(List<ExchangePriceService<?>> exchangePriceServices, PriceAggregatorService priceAggregatorService) {
        this.exchangePriceServices = exchangePriceServices;
        this.priceAggregatorService = priceAggregatorService;

        // Pool size based on the number of implemented ExchangePriceService else 2
        int poolSize = Math.max(exchangePriceServices.size(), 2);
        this.executorService = Executors.newFixedThreadPool(poolSize);
        log.info("Initialized PriceScheduler with {} exchange price sources.", exchangePriceServices.size());
    }

    // Fetch prices from multiple sources concurrently then pass it to PriceAggregatorService
    @Scheduled(fixedRate = FETCH_PRICE_INTERVAL_MS)
    private void fetchPrices() {
        // Use CompletableFuture to chain methods
        List<CompletableFuture<List<PriceResponse>>> futures = exchangePriceServices.stream()
            .map(service -> CompletableFuture.supplyAsync(service::fetchPrices, executorService)
                .exceptionally(ex -> {
                    log.error("Error fetching prices from {}", service.getClass().getSimpleName(), ex);
                    return List.of();
                }))
            .toList();

        // Once all prices come back
        List<PriceResponse> priceResponses = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .toList();

        priceAggregatorService.aggregatePrices(priceResponses);
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
