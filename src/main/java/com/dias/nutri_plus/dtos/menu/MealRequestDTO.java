package com.dias.nutri_plus.dtos.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MealRequestDTO {

  @NotBlank
  private String title;

  @NotEmpty
  private List<FoodRequestDTO> foods;

  @NotNull
  private LocalTime mealTime;
}
