package com.assessment.cts.model;

import com.assessment.cts.enums.ResponseStatus;
import lombok.Data;

import java.util.List;

@Data
public class ResponseListDTO<T>{
    private List<T> list;
    private ResponseStatus responseStatus;
    private String message;
}
