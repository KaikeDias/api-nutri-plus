package com.dias.nutri_plus.controllers;

import com.dias.nutri_plus.dtos.patient.PatientFilterDTO;
import com.dias.nutri_plus.dtos.patient.PatientRequestDTO;
import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.services.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.util.List;

import static com.dias.nutri_plus.utils.constants.CommonConstants.TOKEN;
import static com.dias.nutri_plus.utils.constants.PatientControllerConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(PatientController.class)
@AutoConfigureRestTestClient
class PatientControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private PatientService patientService;

  @MockitoBean
  private JwtDecoder jwtDecoder;

  @BeforeEach
  void setupJwt() {
    Jwt jwt = Jwt.withTokenValue(TOKEN)
        .header("alg", "none")
        .claim("sub", "user-id")
        .claim("scope", "ROLE_USER")
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plusSeconds(3600))
        .build();

    when(jwtDecoder.decode(anyString())).thenReturn(jwt);
  }

  static class PatientPageDTO {
    public List<PatientResponseDTO> content;
    public boolean empty;
    public boolean first;
    public boolean last;
    public int number;
    public int numberOfElements;
    public int size;
    public long totalElements;
    public int totalPages;
  }

  @Test
  void shouldCreatePatient() {
    PatientRequestDTO request = new PatientRequestDTO(
        PATIENT1.getName(),
        PATIENT1.getCpf(),
        PATIENT1.getEmail(),
        PATIENT1.getPhone(),
        PATIENT1.getHeight(),
        PATIENT1.getWeight()
    );

    when(patientService.create(any(PatientRequestDTO.class))).thenReturn(PATIENT1);

    restTestClient.post()
        .uri("/patients")
        .headers(h -> h.setBearerAuth(TOKEN))
        .body(request)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location")
        .expectBody(PatientResponseDTO.class)
        .isEqualTo(PATIENT1);
  }

  @Test
  void shouldSearchPatients() {
    Page<PatientResponseDTO> page = new PageImpl<>(List.of(PATIENT1, PATIENT2));

    when(patientService.searchPatients(any(PatientFilterDTO.class), any(Pageable.class)))
        .thenReturn(page);

    PatientPageDTO responseDto = restTestClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/patients")
            .queryParam("name", "Joao")
            .build())
        .headers(h -> h.setBearerAuth(TOKEN))
        .exchange()
        .expectStatus().isOk()
        .expectBody(PatientPageDTO.class)
        .returnResult()
        .getResponseBody();

    Page<PatientResponseDTO> responsePage = new PageImpl<>(
        responseDto.content,
        PageRequest.of(responseDto.number, responseDto.size),
        responseDto.totalElements
    );

    assertNotNull(responsePage);
    assertEquals(2, responsePage.getNumberOfElements());
    assertTrue(responsePage.getContent().stream().anyMatch(p -> p.getName().equals(PATIENT1.getName())));
    assertTrue(responsePage.getContent().stream().anyMatch(p -> p.getName().equals(PATIENT2.getName())));
  }

  @Test
  void shouldGetPatientById() {
    Patient patient = new Patient();
    patient.setId(PATIENT1_ID);
    patient.setName(PATIENT1.getName());
    patient.setCpf(PATIENT1.getCpf());
    patient.setEmail(PATIENT1.getEmail());

    when(patientService.findById(PATIENT1_ID)).thenReturn(patient);

    restTestClient.get()
        .uri("/patients/{id}", PATIENT1_ID)
        .headers(h -> h.setBearerAuth(TOKEN))
        .exchange()
        .expectStatus().isOk()
        .expectBody(Patient.class)
        .isEqualTo(patient);
  }
}
