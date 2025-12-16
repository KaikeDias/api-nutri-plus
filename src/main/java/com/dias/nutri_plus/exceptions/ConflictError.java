package com.dias.nutri_plus.exceptions;

import java.io.Serial;

public class ConflictError extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ConflictError(String message) {
    super(message);
  }
}
