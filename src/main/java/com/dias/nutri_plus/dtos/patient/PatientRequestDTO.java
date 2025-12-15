package com.dias.nutri_plus.dtos.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientRequestDTO {
  @NotBlank
  private String name;

  @NotBlank
  @Pattern(regexp = "\\d{11}", message = "CPF deve conter exatamente 11 dígitos")
  private String cpf;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String phone;

  private Double height;

  private Double weight;
}
