package com.assessment.cts.service.impl;

import com.assessment.cts.entity.Currency;
import com.assessment.cts.repository.CurrencyRepository;
import com.assessment.cts.service.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Override
    public Currency getCurrencyByCode(String code) {
        return this.currencyRepository.findByCode(code).orElse(null);
    }
}
