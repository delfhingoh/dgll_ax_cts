package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Price;
import com.assessment.cts.entity.Product;
import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.repository.PriceRepository;
import com.assessment.cts.repository.ProductRepository;
import com.assessment.cts.service.HelperUtility;
import com.assessment.cts.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PriceServiceImpl implements PriceService {
    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final HelperUtility helper;

    @Override
    public ResponseDTO<PriceDTO> saveThisPrice(Price price) {
        try {
            this.priceRepository.save(price);
            return this.helper.transformToResponseDTO(mapToPriceDTO(price), ResponseStatus.SUCCESS, "Saved price successfully");
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(mapToPriceDTO(price), ResponseStatus.ERROR, "Unable to save price: " + price.getProduct().getSymbol());
        }
    }

    @Override
    public ResponseDTO<PriceDTO> getLatestPrice(String ccyPair) {
        try {
            Optional<Product> productOptional = Optional.ofNullable(this.productRepository.findBySymbol(ccyPair));
            if (productOptional.isEmpty()) {
                return this.helper.transformToResponseDTO(null, ResponseStatus.INVALID, "This " + ccyPair + " is not available in our system.");
            } else {
                Price latestPrice = this.priceRepository.findFirstByProductOrderByCreatedAtDesc(productOptional.get());
                if (latestPrice == null) {
                    return this.helper.transformToResponseDTO(null, ResponseStatus.INVALID, "There is no latest price for " + ccyPair + ".");
                }
                return this.helper.transformToResponseDTO(mapToPriceDTO(latestPrice), ResponseStatus.SUCCESS, "Retrieved latest price successfully.");
            }
        } catch (Exception e) {
            return this.helper.transformToResponseDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
        }
    }

    @Override
    public ResponseListDTO<PriceDTO> getAllLatestPrices() {
        try {
            List<Price> allProductLatestPrices = this.priceRepository.findLatestPricePerProduct();
            if (allProductLatestPrices.isEmpty()) {
                return this.helper.transformToResponseListDTO(List.of(), ResponseStatus.INVALID, "There are no prices for any product.");
            } else {
                List<PriceDTO> priceDTOList = allProductLatestPrices.stream()
                        .map(this::mapToPriceDTO)
                        .toList();
                return this.helper.transformToResponseListDTO(priceDTOList, ResponseStatus.SUCCESS, "Retrieved all prices for any product.");
            }
        } catch(Exception e) {
            return this.helper.transformToResponseListDTO(null, ResponseStatus.ERROR, "Something went wrong. Please try again later.");
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
