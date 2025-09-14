package com.assessment.cts.service;

import com.assessment.cts.entity.Price;
import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.api.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PriceAggregatorService {
    private final ProductService productService;
    private final PriceService priceService;

    public void aggregatePrices(List<PriceResponse> priceResponses) {
        List<Product> activeProducts = this.productService.getProductsByStatus(Status.ACTIVE);
        if (activeProducts.isEmpty()) {
            log.info("No active products found. Unable to aggregate prices.");
            return;
        }
        // Putting active products symbol, "ETHUSDT", into Set (Although List might be okay since Product have Constraints)
        Set<String> activeProductsBySymbol = activeProducts.stream()
                .map(Product::getSymbol)
                .collect(Collectors.toSet());

        // Filter the list of prices from multiple sources to get only prices for active product
        Map<String, Product> productMap = activeProducts.stream()
                .collect(Collectors.toMap(Product::getSymbol, product -> product));

        List<PriceResponse> filtered = priceResponses.stream()
                .filter(priceResponse -> activeProductsBySymbol.contains(priceResponse.getSymbol()))
                .toList();

        // Group the active product prices by product
        // <ETCUSDT, List of prices>
        // <BTCUSDT, List of prices>
        Map<String, List<PriceResponse>> groupedByProduct = filtered.stream()
                        .collect(Collectors.groupingBy(PriceResponse::getSymbol));

        findAndSaveBestPrice(groupedByProduct);
    }

    private void findAndSaveBestPrice(Map<String, List<PriceResponse>> groupedByProduct) {
        if (groupedByProduct.isEmpty()) {
            log.info("No prices by product found. Unable to aggregate prices.");
            return;
        } else {
            for (Map.Entry<String, List<PriceResponse>> entry : groupedByProduct.entrySet()) {
                String symbol = entry.getKey();
                List<PriceResponse> priceResponses = entry.getValue();

                // Buy Low Sell High
                // Since BID is to sell, then the highest amount should be the best
                PriceResponse bestBidPriceResponse = priceResponses.stream()
                        .max(Comparator.comparing(PriceResponse::getBidPrice))
                        .orElse(null);
                // Since ASK is to buy, then the lowest amount should be the best
                PriceResponse bestAskPriceResponse = priceResponses.stream()
                        .min(Comparator.comparing(PriceResponse::getAskPrice))
                        .orElse(null);

                // There should be a BID and ASK
                if (bestBidPriceResponse != null && bestAskPriceResponse != null) {
                    Price price = new Price();
                    Product product = this.productService.getProductBySymbol(symbol);
                    price.setProduct(product);
                    price.setBidPrice(bestBidPriceResponse.getBidPrice());
                    price.setBidQuantity(bestBidPriceResponse.getBidQuantity());
                    price.setBidSource(bestBidPriceResponse.getSource());
                    price.setAskPrice(bestAskPriceResponse.getAskPrice());
                    price.setAskQuantity(bestAskPriceResponse.getAskQuantity());
                    price.setAskSource(bestAskPriceResponse.getSource());
                    ResponseDTO<PriceDTO> savePriceResult = this.priceService.saveThisPrice(price);
                    log.info("Save price result: {}", savePriceResult.getResponseStatus());
                } else {
                    log.info("Missing bid or ask price. Unable to save best prices.");
                }
            }
        }
    }
}
