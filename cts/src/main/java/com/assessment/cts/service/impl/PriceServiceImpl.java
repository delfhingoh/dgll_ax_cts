package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Price;
import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.repository.PriceRepository;
import com.assessment.cts.repository.ProductRepository;
import com.assessment.cts.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;

    @Override
    public PriceDTO getLatestPrice(String ccyPair) {
        try {
            Optional<Product> productOptional = Optional.ofNullable(this.productRepository.findBySymbol(ccyPair));
            if (productOptional.isEmpty()) {
                PriceDTO priceDTO = new PriceDTO();
                priceDTO.setStatus(ResponseStatus.INVALID);
                priceDTO.setMessage("This " + ccyPair + " is not available in our system.");
                return priceDTO;
            } else {
                Price latestPrice = this.priceRepository.findFirstByProductOrderByCreatedAtDesc(productOptional.get());
                if (latestPrice == null) {
                    PriceDTO priceDTO = new PriceDTO();
                    priceDTO.setStatus(ResponseStatus.INVALID);
                    priceDTO.setMessage("There is no latest price for " + ccyPair + ".");
                    return priceDTO;
                }
                return mapToPriceDTO(latestPrice);
            }
        } catch (Exception e) {
            PriceDTO priceDTO = new PriceDTO();
            priceDTO.setStatus(ResponseStatus.ERROR);
            priceDTO.setMessage("Something went wrong. Please try again later.");
            return priceDTO;
        }
    }

    @Override
    public List<PriceDTO> getAllLatestPrices() {
        try {
            List<Price> allProductLatestPrices = this.priceRepository.findLatestPricePerProduct();
            if (allProductLatestPrices.isEmpty()) {
                PriceDTO priceDTO = new PriceDTO();
                priceDTO.setStatus(ResponseStatus.INVALID);
                priceDTO.setMessage("There are no prices for any product.");
                return List.of(priceDTO);
            } else {
                return allProductLatestPrices.stream()
                        .map(this::mapToPriceDTO)
                        .toList();
            }
        } catch(Exception e) {
            PriceDTO priceDTO = new PriceDTO();
            priceDTO.setStatus(ResponseStatus.ERROR);
            priceDTO.setMessage("Something went wrong. Please try again later.");
            return List.of(priceDTO);
        }
    }

    private PriceDTO mapToPriceDTO(Price price) {
        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setSymbol(price.getProduct().getSymbol());
        priceDTO.setBidPrice(price.getBidPrice());
        priceDTO.setBidQuantity(price.getBidQuantity());
        priceDTO.setAskPrice(price.getAskPrice());
        priceDTO.setAskQuantity(price.getAskQuantity());
        priceDTO.setUpdatedOn(price.getCreatedAt());
        priceDTO.setStatus(ResponseStatus.SUCCESS);
        return priceDTO;
    }
}
