package com.dias.nutri_plus.dtos.patient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientFilterDTO {
    private String name;
    private String cpf;
}
