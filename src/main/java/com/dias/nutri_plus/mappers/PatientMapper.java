package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper {

  Patient requestToEntity(PatientRequestDTO patientRequestDTO);

  PatientResponseDTO entityToResponseDTO(Patient patient);
}
