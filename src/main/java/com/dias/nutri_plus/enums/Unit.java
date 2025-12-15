package com.dias.nutri_plus.enums;

import lombok.Getter;

@Getter
public enum Unit {
  MILIGRAM("mg"),
  GRAM("g"),
  KILOGRAM("kg"),
  MILLILITER("ml"),
  LITER("l");

  private final String description;

  Unit(String description) {
    this.description = description;
  }
}
