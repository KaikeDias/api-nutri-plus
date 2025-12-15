package com.dias.nutri_plus.dtos.menu;

import com.dias.nutri_plus.enums.Unit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BaseFoodRequestDTO {

  @NotBlank
  private String name;

  @NotBlank
  private String quantity;

  @NotNull
  private Unit unit;

  @NotBlank
  private String homeQuantity;

  @NotBlank
  private String homeUnit;
}
