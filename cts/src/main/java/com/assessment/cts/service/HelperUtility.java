package com.assessment.cts.service;

import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class HelperUtility {
    public <T> ResponseDTO<T> transformToResponseDTO(T data, ResponseStatus status, String message) {
        ResponseDTO<T> responseDTO = new ResponseDTO<>();
        if (data != null) {
            responseDTO.setResponse(data);
        }
        responseDTO.setResponseStatus(status);
        responseDTO.setMessage(message);
        return responseDTO;
    }
}
