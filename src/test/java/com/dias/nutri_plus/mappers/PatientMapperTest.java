package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PatientMapperTest {

    private final PatientMapper patientMapper = Mappers.getMapper(PatientMapper.class);

    @Test
    void shouldMapRequestDTOToEntity() {
        PatientRequestDTO dto = new PatientRequestDTO();
        dto.setName("John");

        Patient patient = patientMapper.requestToEntity(dto);

        assertNotNull(patient);
        assertEquals("John", patient.getName());
    }

    @Test
    void shouldMapEntityToResponseDTO() {
        Patient patient = new Patient();
        patient.setName("Maria");

        PatientResponseDTO response = patientMapper.entityToResponseDTO(patient);

        assertNotNull(response);
        assertEquals("Maria", response.getName());
    }
}
