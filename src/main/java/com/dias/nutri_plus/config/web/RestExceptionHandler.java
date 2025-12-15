package com.dias.nutri_plus.config.web;

import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.utils.RestErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(NotFoundError.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public ResponseEntity<RestErrorMessage> notFoundErrorHandler(NotFoundError e) {
    RestErrorMessage threatResponse = new RestErrorMessage(HttpStatus.NOT_FOUND, e.getMessage());

    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatResponse);
  }
}
