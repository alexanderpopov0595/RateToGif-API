package com.fancysoft.api.exception.handler;

import com.fancysoft.api.exception.model.ApiException;
import com.fancysoft.api.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiError> handleApiException(ApiException e) {
    log.error("ApiException: {}", e.getMessage());
    return ResponseEntity
        .status(e.getStatus())
        .body(ApiError.builder()
            .status(e.getStatus())
            .details(e.getMessage())
            .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleException(Exception e) {
    log.error("Exception: {}", e.getMessage());
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .details(e.getMessage())
            .build());
  }
}
