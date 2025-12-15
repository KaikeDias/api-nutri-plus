package com.dias.nutri_plus.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@AllArgsConstructor
@Data
public class RestErrorMessage {
  private HttpStatus status;
  private String message;
  private Map<String, String> fieldErrors;

  public RestErrorMessage(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }
}