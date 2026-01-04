package org.example.ecommerce.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.common.BaseResponse;
import org.example.ecommerce.common.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class HandleGlobalException {


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<BaseResponse> validationException(ValidationException ex) {
        log.error("validation exception: {} ", ex.toString());
        Result response = new Result();
        response.setSuccessCode(ex.getErrorCode());
        response.setSuccessDescription(ex.getErrorDescription());
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResult(response);
        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

}
