package com.dias.nutri_plus.controllers;

import com.dias.nutri_plus.dtos.patient.PatientFilterDTO;
import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.services.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {
  private final PatientService patientService;

  @Operation(summary = "Create Patient", operationId = "createPatient")
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  public ResponseEntity<PatientResponseDTO> create(@Valid @RequestBody PatientRequestDTO request) {
    PatientResponseDTO patient = patientService.create(request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(patient.getId())
        .toUri();

    return ResponseEntity.created(uri).body(patient);
  }

  @Operation(summary = "Search Patients with or without filters", operationId = "searchPatients")
  @GetMapping
  public ResponseEntity<Page<PatientResponseDTO>> searchPatients(@ParameterObject PatientFilterDTO filters, @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(patientService.searchPatients(filters, pageable));
  }

  @Operation(summary = "Find Patient by ID", operationId = "findPatientById")
  @GetMapping("/{id}")
  public ResponseEntity<Patient> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(patientService.findById(id));
  }
}
