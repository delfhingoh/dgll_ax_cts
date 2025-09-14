package com.assessment.cts.service;

import com.assessment.cts.entity.Currency;

public interface CurrencyService {
    Currency getCurrencyByCode(String code);
}
