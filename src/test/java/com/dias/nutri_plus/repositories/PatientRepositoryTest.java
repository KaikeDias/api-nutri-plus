package com.dias.nutri_plus.repositories;

import com.dias.nutri_plus.entities.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldFindPatientByKeycloakUserIdUsingSpecification() {
        Patient patient = new Patient();
        patient.setName("João Silva");
        patient.setEmail("joao@email.com");
        patient.setCpf("12345678900");
        patient.setPhone("11999999999");
        patient.setKeycloakUserId("user-123");

        patientRepository.save(patient);

        Page<Patient> result = patientRepository.findAll(
                (root, query, cb) ->
                        cb.equal(root.get("keycloakUserId"), "user-123"),
                Pageable.unpaged()
        );

        assertEquals(1, result.getTotalElements());
    }

}

