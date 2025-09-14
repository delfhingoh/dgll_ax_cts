package com.assessment.cts.controller;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.service.PriceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/price")
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/bestPrices/{ccyPair}")
    PriceDTO getThisBestPrice(@PathVariable String ccyPair) {
        return this.priceService.getLatestPrice(ccyPair);
    }

    @GetMapping("/bestPrices")
    List<PriceDTO> getAllBestPrice() {
        return this.priceService.getAllLatestPrices();
    }
}
