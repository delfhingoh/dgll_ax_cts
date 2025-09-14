package com.assessment.cts.model;

import com.assessment.cts.enums.ResponseStatus;
import lombok.Data;

@Data
public class ResponseDTO<T>{
    private T response;
    private ResponseStatus responseStatus;
    private String message;
}
