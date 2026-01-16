package com.dias.nutri_plus.utils.constants;

import com.dias.nutri_plus.dtos.menu.BaseFoodRequestDTO;
import com.dias.nutri_plus.dtos.menu.FoodRequestDTO;
import com.dias.nutri_plus.entities.Food;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.enums.Unit;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class MenuControllerConstants {

  public static final UUID PATIENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
  public static final UUID MEAL1_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
  public static final UUID MEAL2_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

  public static final Patient PATIENT = Patient.builder()
      .id(PATIENT_ID)
      .name("Joao Silva")
      .cpf("11111111111")
      .email("joao@email.com")
      .height(1.75)
      .weight(75.0)
      .keycloakUserId(UUID.randomUUID().toString())
      .build();

  public static final BaseFoodRequestDTO ARROZ_BASE = new BaseFoodRequestDTO(
      "Arroz", "100", Unit.GRAM, "1/2 xícara", "xícara"
  );

  public static final BaseFoodRequestDTO FEIJAO_BASE = new BaseFoodRequestDTO(
      "Feijão", "50", Unit.GRAM, "1/4 xícara", "xícara"
  );

  public static final FoodRequestDTO FOOD_REQUEST1 = new FoodRequestDTO(ARROZ_BASE, List.of());
  public static final FoodRequestDTO FOOD_REQUEST2 = new FoodRequestDTO(FEIJAO_BASE, List.of());

  public static final Food FOOD_ENTITY1 = new Food(
      UUID.randomUUID(),
      "Arroz", "100", Unit.GRAM, "1/2 xícara", "xícara",
      List.of(), null, null
  );

  public static final Food FOOD_ENTITY2 = new Food(
      UUID.randomUUID(),
      "Feijão", "50", Unit.GRAM, "1/4 xícara", "xícara",
      List.of(), null, null
  );

  public static final Meal MEAL1 = new Meal(
      MEAL1_ID,
      "Café da manhã",
      LocalTime.of(8, 0),
      List.of(FOOD_ENTITY1, FOOD_ENTITY2),
      null
  );

  public static final Meal MEAL2 = new Meal(
      MEAL2_ID,
      "Almoço",
      LocalTime.of(12, 0),
      List.of(FOOD_ENTITY1),
      null
  );

  public static final Menu MENU = new Menu(UUID.randomUUID(), PATIENT, List.of(MEAL1, MEAL2));
}
