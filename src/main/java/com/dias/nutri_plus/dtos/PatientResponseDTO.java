package com.dias.nutri_plus.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
  private UUID id;

  private String name;

  private String cpf;

  private String email;

  private String phone;

  private Double height;

  private Double weight;
}
