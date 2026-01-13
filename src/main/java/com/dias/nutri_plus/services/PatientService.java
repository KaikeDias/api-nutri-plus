package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.PatientMapper;
import com.dias.nutri_plus.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
  private final PatientRepository patientRepository;
  private final PatientMapper patientMapper;
  private final AuthService authService;

  public PatientResponseDTO create(PatientRequestDTO dto) {
    validateDuplicity(dto.getCpf());

    Patient patient = patientMapper.requestToEntity(dto);
    patient.setKeycloakUserId(authService.getCurrentUserSub());

    return patientMapper.entityToResponseDTO(patientRepository.save(patient));
  }

  public Patient findById(UUID id) {
    return patientRepository
            .findByIdAndKeycloakUserId(id, authService.getCurrentUserSub())
            .orElseThrow(() ->
                    new NotFoundError("Patient not found")
            );
  }

  public List<PatientResponseDTO> findAll() {
    List<Patient> patients = patientRepository.findAllByKeycloakUserId(authService.getCurrentUserSub());

    return patients.stream()
        .map(patientMapper::entityToResponseDTO)
        .toList();
  }

  private void validateDuplicity(String cpf) {
    Optional<Patient> existingPatient = patientRepository.findByCpfAndKeycloakUserId(cpf, authService.getCurrentUserSub());

    if(existingPatient.isPresent()) {
      throw new ConflictError("A patient with this CPF already exists!");
    }
  }
}
