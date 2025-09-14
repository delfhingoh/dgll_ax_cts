package com.assessment.cts.controller;
import com.assessment.cts.model.PriceDTO;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import com.assessment.cts.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/price")
public class PriceController {

    private final PriceService priceService;

    @GetMapping("/bestPrices/{ccyPair}")
    ResponseDTO<PriceDTO> getThisBestPrice(@PathVariable String ccyPair) {
        return this.priceService.getLatestPrice(ccyPair);
    }

    @GetMapping("/bestPrices")
    ResponseListDTO<PriceDTO> getAllBestPrice() {
        return this.priceService.getAllLatestPrices();
    }
}
