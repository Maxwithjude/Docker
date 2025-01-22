package com.be.byeoldam.exception;

import com.be.byeoldam.common.ResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.be.byeoldam")
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ResponseTemplate<String> handleCustomException(CustomException e) {
        log.debug("handleCustomException : {}", e.getMessage());
        return ResponseTemplate.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseTemplate<String> handleException(Exception e) {
        log.error("handleException : {}", e.getMessage());
        return ResponseTemplate.fail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseTemplate<String> handleValidationException(MethodArgumentNotValidException e) {
        log.debug("Validation error: {}", e.getMessage());
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed.");
        return ResponseTemplate.fail(errorMessage);
    }
}
