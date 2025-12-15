package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.PatientRequestDTO;
import com.dias.nutri_plus.dtos.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.PatientMapper;
import com.dias.nutri_plus.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
  private final PatientRepository patientRepository;
  private final PatientMapper patientMapper;

  public PatientResponseDTO create(PatientRequestDTO dto) {
    Patient patient = patientMapper.requestToEntity(dto);

    return patientMapper.entityToResponseDTO(patientRepository.save(patient));
  }

  public PatientResponseDTO findById(UUID id) {
    Patient patient = patientRepository.findById(id).orElseThrow(() -> new NotFoundError("Patient not found"));

    return patientMapper.entityToResponseDTO(patient);
  }

  public List<PatientResponseDTO> findAll() {
    List<Patient> patients = patientRepository.findAll();

    return patients.stream()
        .map(patientMapper::entityToResponseDTO)
        .toList();
  }
}
