package com.assessment.cts.model.api.houbi;

import lombok.Data;

import java.util.List;

@Data
public class HoubiTickerResponse {
    private List<HoubiTicker> data;
}
