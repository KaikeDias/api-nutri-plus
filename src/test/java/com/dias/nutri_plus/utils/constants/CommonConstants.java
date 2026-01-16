package com.dias.nutri_plus.utils.constants;

import org.springframework.data.domain.PageRequest;

public class CommonConstants {
  public static final String TOKEN = "fake-jwt-token";

  public static final PageRequest DEFAULT_PAGE = PageRequest.of(0, 20);
}
