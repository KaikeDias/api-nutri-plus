package com.dias.nutri_plus.dtos.menu;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequestDTO {

  @NotNull
  private BaseFoodRequestDTO food;

  private List<BaseFoodRequestDTO> substitutions;
}
