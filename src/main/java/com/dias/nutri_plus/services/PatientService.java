package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.patient.PatientFilterDTO;
import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.PatientMapper;
import com.dias.nutri_plus.repositories.PatientRepository;
import com.dias.nutri_plus.repositories.specifications.PatientSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

  public Page<PatientResponseDTO> searchPatients(PatientFilterDTO filters, Pageable pageable) {
      String keycloakUserId = authService.getCurrentUserSub();

      Specification<Patient> spec = Specification
              .where(PatientSpecification.filter(filters))
              .and(((root, query, cb) ->
                      cb.equal(root.get("keycloakUserId"), keycloakUserId)
              ));

      Page<Patient> patients = patientRepository.findAll(spec, pageable);

      return patients.map(patientMapper::entityToResponseDTO);
  }

  private void validateDuplicity(String cpf) {
    Optional<Patient> existingPatient = patientRepository.findByCpfAndKeycloakUserId(cpf, authService.getCurrentUserSub());

    if(existingPatient.isPresent()) {
      throw new ConflictError("A patient with this CPF already exists!");
    }
  }
}
