package com.assessment.cts.service;

import com.assessment.cts.entity.Price;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;

public interface PriceService {
    ResponseDTO<PriceDTO> getLatestPrice(String ccyPair);
    ResponseListDTO<PriceDTO> getAllLatestPrices();
    ResponseDTO<PriceDTO> saveThisPrice(Price price);
}
