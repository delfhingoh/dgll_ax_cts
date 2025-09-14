package com.assessment.cts.service;

import com.assessment.cts.model.PriceDTO;

import java.util.List;

public interface PriceService {
    PriceDTO getLatestPrice(String ccyPair);
    List<PriceDTO> getAllLatestPrices();
}
