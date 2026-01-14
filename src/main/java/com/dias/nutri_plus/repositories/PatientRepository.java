package com.dias.nutri_plus.repositories;

import com.dias.nutri_plus.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID>, JpaSpecificationExecutor<Patient> {

  Optional<Patient> findByCpfAndKeycloakUserId(String cpf, String keycloakUserId);

  Optional<Patient> findByIdAndKeycloakUserId(UUID id, String keycloakUserId);
}
