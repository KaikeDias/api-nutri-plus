package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.patient.PatientFilterDTO;
import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.PatientMapper;
import com.dias.nutri_plus.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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

    private static final String USER_ID = "12345";
    private static final String FIRST_CPF = "111.111.111-11";
    private static final String SECOND_CPF = "222.222.222-22";
    private static final String PATIENT_NAME = "João Silva";

    @Test
    void createShouldSavePatientWhenCpfNotExists() {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setCpf("54584471070");

        Patient patientEntity = new Patient();
        patientEntity.setCpf(requestDTO.getCpf());

        Patient savedPatient = new Patient();
        savedPatient.setCpf(requestDTO.getCpf());
        savedPatient.setKeycloakUserId(USER_ID);

        PatientResponseDTO responseDTO = new PatientResponseDTO();
        responseDTO.setCpf(requestDTO.getCpf());

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findByCpfAndKeycloakUserId(requestDTO.getCpf(), USER_ID))
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
    void createShouldThrowConflictErrorWhenCpfExists() {
        PatientRequestDTO requestDTO = new PatientRequestDTO();
        requestDTO.setCpf("111.222.333-44");

        Patient existingPatient = new Patient();
        existingPatient.setCpf(requestDTO.getCpf());

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findByCpfAndKeycloakUserId(requestDTO.getCpf(), USER_ID))
                .thenReturn(Optional.of(existingPatient));

        ConflictError exception = assertThrows(ConflictError.class, () -> patientService.create(requestDTO));

        assertEquals("A patient with this CPF already exists!", exception.getMessage());
        verify(patientRepository, never()).save(any());
    }

    @Test
    void findByIdShouldReturnPatientWhenFound() {
        UUID id = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(id);
        patient.setKeycloakUserId(USER_ID);

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findByIdAndKeycloakUserId(id, USER_ID)).thenReturn(Optional.of(patient));

        Patient result = patientService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void findByIdShouldThrowNotFoundErrorWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findByIdAndKeycloakUserId(id, USER_ID)).thenReturn(Optional.empty());

        NotFoundError exception = assertThrows(NotFoundError.class, () -> patientService.findById(id));
        assertEquals("Patient not found", exception.getMessage());
    }

    @Test
    void searchPatientsShouldReturnPagedPatientsWithFilters() {

        // ---- Arrange ----
        Patient patient1 = new Patient();
        patient1.setCpf(FIRST_CPF);

        Patient patient2 = new Patient();
        patient2.setCpf(SECOND_CPF);

        Page<Patient> patientsPage = new PageImpl<>(List.of(patient1, patient2));

        PatientFilterDTO filterDTO = new PatientFilterDTO();
        Pageable pageable = PageRequest.of(0, 10);

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findAll(
                ArgumentMatchers.<Specification<Patient>>any(),
                eq(pageable)
        )).thenReturn(patientsPage);

        PatientResponseDTO responseFirst = new PatientResponseDTO();
        responseFirst.setCpf(FIRST_CPF);

        when(patientMapper.entityToResponseDTO(patient1)).thenReturn(responseFirst);

        PatientResponseDTO responseSecond = new PatientResponseDTO();
        responseSecond.setCpf(SECOND_CPF);

        when(patientMapper.entityToResponseDTO(patient2)).thenReturn(responseSecond);

        Page<PatientResponseDTO> result = patientService.searchPatients(filterDTO, pageable);

        assertEquals(2, result.getTotalElements());
        assertEquals(FIRST_CPF, result.getContent().get(0).getCpf());
        assertEquals(SECOND_CPF, result.getContent().get(1).getCpf());
    }

    @Test
    void searchPatientsShouldFilterByName() {

        Patient patient = new Patient();
        patient.setName(PATIENT_NAME);
        patient.setCpf(FIRST_CPF);

        Page<Patient> patientsPage = new PageImpl<>(List.of(patient));

        PatientFilterDTO filterDTO = new PatientFilterDTO();
        filterDTO.setName("João");

        Pageable pageable = PageRequest.of(0, 10);

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findAll(
                ArgumentMatchers.<Specification<Patient>>any(),
                eq(pageable)
        )).thenReturn(patientsPage);

        PatientResponseDTO patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setName(PATIENT_NAME);
        patientResponseDTO.setCpf(FIRST_CPF);

        when(patientMapper.entityToResponseDTO(patient))
                .thenReturn(patientResponseDTO);

        Page<PatientResponseDTO> result = patientService.searchPatients(filterDTO, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(PATIENT_NAME, result.getContent().get(0).getName());
    }

    @Test
    void searchPatientsShouldFilterByCpf() {

        Patient patient = new Patient();
        patient.setCpf(SECOND_CPF);

        Page<Patient> patientsPage = new PageImpl<>(List.of(patient));

        PatientFilterDTO filterDTO = new PatientFilterDTO();
        filterDTO.setCpf(SECOND_CPF);

        Pageable pageable = PageRequest.of(0, 10);

        when(authService.getCurrentUserSub()).thenReturn(USER_ID);

        when(patientRepository.findAll(
                ArgumentMatchers.<Specification<Patient>>any(),
                eq(pageable)
        )).thenReturn(patientsPage);

        PatientResponseDTO responseSecond = new PatientResponseDTO();
        responseSecond.setCpf(SECOND_CPF);

        when(patientMapper.entityToResponseDTO(patient))
                .thenReturn(responseSecond);

        Page<PatientResponseDTO> result = patientService.searchPatients(filterDTO, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(SECOND_CPF, result.getContent().get(0).getCpf());
    }
}