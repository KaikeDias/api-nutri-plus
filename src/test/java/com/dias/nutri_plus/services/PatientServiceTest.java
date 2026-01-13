package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.PatientMapper;
import com.dias.nutri_plus.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private PatientService patientService;

    private final String userId = "12345";

    @Test
    void create_shouldSavePatient_whenCpfNotExists() {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setCpf("54584471070");

        Patient patientEntity = new Patient();
        patientEntity.setCpf(requestDTO.getCpf());

        Patient savedPatient = new Patient();
        savedPatient.setCpf(requestDTO.getCpf());
        savedPatient.setKeycloakUserId(userId);

        PatientResponseDTO responseDTO = new PatientResponseDTO();
        responseDTO.setCpf(requestDTO.getCpf());

        when(authService.getCurrentUserSub()).thenReturn(userId);

        when(patientRepository.findByCpfAndKeycloakUserId(requestDTO.getCpf(), userId))
                .thenReturn(Optional.empty());
        when(patientMapper.requestToEntity(requestDTO)).thenReturn(patientEntity);
        when(patientRepository.save(patientEntity)).thenReturn(savedPatient);
        when(patientMapper.entityToResponseDTO(savedPatient)).thenReturn(responseDTO);

        PatientResponseDTO result = patientService.create(requestDTO);

        assertNotNull(result);
        assertEquals(requestDTO.getCpf(), result.getCpf());
        verify(patientRepository).save(patientEntity);
    }


    @Test
    void create_shouldThrowConflictError_whenCpfExists() {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setCpf("111.222.333-44");

        Patient existingPatient = new Patient();
        existingPatient.setCpf(requestDTO.getCpf());

        when(authService.getCurrentUserSub()).thenReturn(userId);

        when(patientRepository.findByCpfAndKeycloakUserId(requestDTO.getCpf(), userId))
                .thenReturn(Optional.of(existingPatient));

        ConflictError exception = assertThrows(ConflictError.class, () -> {
            patientService.create(requestDTO);
        });

        assertEquals("A patient with this CPF already exists!", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void findById_shouldReturnPatient_whenFound() {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(id);
        patient.setKeycloakUserId(userId);

        when(authService.getCurrentUserSub()).thenReturn(userId);

        when(patientRepository.findByIdAndKeycloakUserId(id, userId)).thenReturn(Optional.of(patient));

        Patient result = patientService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void findById_shouldThrowNotFoundError_whenNotFound() {
        UUID id = UUID.randomUUID();

        when(authService.getCurrentUserSub()).thenReturn(userId);

        when(patientRepository.findByIdAndKeycloakUserId(id, userId)).thenReturn(Optional.empty());

        NotFoundError exception = assertThrows(NotFoundError.class, () -> patientService.findById(id));
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void findAll_shouldReturnListOfPatients() {
        Patient patient1 = new Patient();
        patient1.setCpf("111.111.111-11");
        Patient patient2 = new Patient();
        patient2.setCpf("222.222.222-22");

        List<Patient> patients = List.of(patient1, patient2);

        when(authService.getCurrentUserSub()).thenReturn(userId);

        when(patientRepository.findAllByKeycloakUserId(userId)).thenReturn(patients);
        when(patientMapper.entityToResponseDTO(patient1)).thenReturn(new PatientResponseDTO() {{ setCpf(patient1.getCpf()); }});
        when(patientMapper.entityToResponseDTO(patient2)).thenReturn(new PatientResponseDTO() {{ setCpf(patient2.getCpf()); }});

        List<PatientResponseDTO> result = patientService.findAll();

        assertEquals(2, result.size());
        assertEquals("111.111.111-11", result.get(0).getCpf());
        assertEquals("222.222.222-22", result.get(1).getCpf());
    }
}