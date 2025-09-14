package com.assessment.cts.service;

import com.assessment.cts.entity.Price;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;

import java.util.List;

public interface PriceService {
    PriceDTO getLatestPrice(String ccyPair);
    List<PriceDTO> getAllLatestPrices();
    ResponseDTO<PriceDTO> saveThisPrice(Price price);
}
