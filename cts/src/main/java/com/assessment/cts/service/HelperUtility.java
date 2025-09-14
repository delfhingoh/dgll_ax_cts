package com.assessment.cts.service;

import com.assessment.cts.enums.ResponseStatus;
import com.assessment.cts.model.ResponseDTO;
import com.assessment.cts.model.ResponseListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public <T> ResponseListDTO<T> transformToResponseListDTO(List<T> data, ResponseStatus status, String message) {
        ResponseListDTO<T> responseListDTO = new ResponseListDTO<>();
        if (data != null) {
            responseListDTO.setList(data);
        }
        responseListDTO.setResponseStatus(status);
        responseListDTO.setMessage(message);
        return responseListDTO;
    }
}
