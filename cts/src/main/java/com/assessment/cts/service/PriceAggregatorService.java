package com.assessment.cts.service;

import com.assessment.cts.entity.Price;
import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.Status;
import com.assessment.cts.model.api.PriceResponse;
import com.assessment.cts.repository.CurrencyRepository;
import com.assessment.cts.repository.PriceRepository;
import com.assessment.cts.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class PriceAggregatorService {

    private final ProductRepository productRepository;
    private final CurrencyRepository currencyRepository;
    private final PriceRepository priceRepository;
    private final ProductService productService;

    public void aggregatePrices(List<PriceResponse> priceResponses) {
        List<Product> activeProducts = this.productService.getProductsByStatus(Status.ACTIVE);
        if (activeProducts.isEmpty()) {
            log.info("No active products found. Unable to aggregate prices.");
            return;
        } else {
            Set<String> activeProductsBySymbol = activeProducts.stream()
                    .map(Product::getSymbol)
                    .collect(Collectors.toSet());
            List<PriceResponse> filtered = priceResponses.stream()
                    .filter(priceResponse -> activeProductsBySymbol.contains(priceResponse.getSymbol()))
                    .toList();

            // Group the active product prices into product
            // ETCUSDT ---> <List of prices>
            // BTCUSDT ---> <List of prices>
            Map<String, List<PriceResponse>> groupedByProduct = filtered.stream()
                            .collect(Collectors.groupingBy(PriceResponse::getSymbol));

            findAndSaveBestPrice(groupedByProduct);
        }
    }

    private void findAndSaveBestPrice(Map<String, List<PriceResponse>> groupedByProduct) {
        if (groupedByProduct.isEmpty()) {
            log.info("No prices found. Unable to aggregate prices.");
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
                    Product product = this.productRepository.findBySymbol(symbol);
                    price.setProduct(product);
                    price.setBidPrice(bestBidPriceResponse.getBidPrice());
                    price.setBidQuantity(bestBidPriceResponse.getBidQuantity());
                    price.setBidSource(bestBidPriceResponse.getSource());
                    price.setAskPrice(bestAskPriceResponse.getAskPrice());
                    price.setAskQuantity(bestAskPriceResponse.getAskQuantity());
                    price.setAskSource(bestAskPriceResponse.getSource());
                    this.priceRepository.save(price);
                } else {
                    log.info("Missing bid or ask price. Unable to save best prices.");
                }
            }
        }
    }
}
