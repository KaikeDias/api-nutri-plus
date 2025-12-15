package com.dias.nutri_plus.exceptions;

public class NotFoundError extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public NotFoundError(String message) {
    super(message);
  }
}
