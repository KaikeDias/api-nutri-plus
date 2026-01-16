package com.dias.nutri_plus.utils.constants;

import com.dias.nutri_plus.dtos.patient.PatientResponseDTO;

import java.util.UUID;

public class PatientControllerConstants {
  public static final UUID PATIENT1_ID = UUID.fromString("f92ae9bc-b0e9-4958-8166-8e39c299334c");
  public static final UUID PATIENT2_ID = UUID.fromString("fbf1b9e3-7b16-4a50-afbb-5ed65bd4cce7");

  public static final PatientResponseDTO PATIENT1 = new PatientResponseDTO(
      PATIENT1_ID,
      "Joao Silva",
      "11111111111",
      "joao@email.com",
      "11999999999",
      1.75,
      75.0,
      UUID.randomUUID().toString()
  );

  public static final PatientResponseDTO PATIENT2 = new PatientResponseDTO(
      PATIENT2_ID,
      "Maria Souza",
      "22222222222",
      "maria@email.com",
      "11988888888",
      1.65,
      60.0,
      UUID.randomUUID().toString()
  );
}
