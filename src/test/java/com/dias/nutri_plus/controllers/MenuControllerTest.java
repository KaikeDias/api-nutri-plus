package com.dias.nutri_plus.controllers;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.services.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static com.dias.nutri_plus.utils.constants.CommonConstants.DEFAULT_PAGE;
import static com.dias.nutri_plus.utils.constants.CommonConstants.TOKEN;
import static com.dias.nutri_plus.utils.constants.MenuControllerConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(MenuController.class)
@AutoConfigureRestTestClient
class MenuControllerTest {

  @Autowired
  private RestTestClient restTestClient;

  @MockitoBean
  private MenuService menuService;

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

  static class MealPageDTO {
    public List<Meal> content;
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
  void shouldAddMeal() {
    MealRequestDTO request = new MealRequestDTO(
        "Café da manhã",
        List.of(FOOD_REQUEST1, FOOD_REQUEST2),
        LocalTime.of(8, 0)
    );

    when(menuService.addMeal(PATIENT_ID, request)).thenReturn(MENU);

    restTestClient.post()
        .uri("/patients/{patientId}/menu/meals", PATIENT_ID)
        .headers(h -> h.setBearerAuth(TOKEN))
        .body(request)
        .exchange()
        .expectStatus().isCreated()
        .expectHeader().exists("Location")
        .expectBody(Menu.class)
        .consumeWith(response -> {
          Menu body = response.getResponseBody();
          assertNotNull(body);
          assertEquals(MENU.getId(), body.getId());
          assertEquals(MENU.getMeals().size(), body.getMeals().size());

          assertTrue(body.getMeals().stream()
              .anyMatch(m -> m.getTitle().equals("Café da manhã")));
          assertTrue(body.getMeals().stream()
              .anyMatch(m -> m.getTitle().equals("Almoço")));
        });

  }

  @Test
  void shouldGetMenu() {
    Page<Meal> page = new PageImpl<>(List.of(MEAL1, MEAL2), DEFAULT_PAGE, 2);
    when(menuService.findMeals(any(UUID.class), any())).thenReturn(page);

    MealPageDTO responseDto = restTestClient.get()
        .uri("/patients/{patientId}/menu", PATIENT_ID)
        .headers(h -> h.setBearerAuth(TOKEN))
        .exchange()
        .expectStatus().isOk()
        .expectBody(MealPageDTO.class)
        .returnResult()
        .getResponseBody();

    Page<Meal> responsePage = new PageImpl<>(
        responseDto.content,
        PageRequest.of(responseDto.number, responseDto.size),
        responseDto.totalElements
    );

    assertNotNull(responsePage);
    assertEquals(2, responsePage.getNumberOfElements());
    assertTrue(responsePage.getContent().stream().anyMatch(m -> m.getTitle().equals(MEAL1.getTitle())));
    assertTrue(responsePage.getContent().stream().anyMatch(m -> m.getTitle().equals(MEAL2.getTitle())));
  }

  @Test
  void shouldUpdateMeal() {
    MealRequestDTO request = new MealRequestDTO(
        "Café reforçado",
        List.of(FOOD_REQUEST1),
        LocalTime.of(9, 0)
    );

    Meal updatedMeal = new Meal(
        MEAL1_ID,
        request.getTitle(),
        request.getMealTime(),
        List.of(FOOD_ENTITY1),
        null
    );

    when(menuService.updateMeal(MEAL1_ID, request)).thenReturn(updatedMeal);

    Meal response = restTestClient.put()
        .uri("/patients/{patientId}/menu/meals/{mealId}", PATIENT_ID, MEAL1_ID)
        .headers(h -> h.setBearerAuth(TOKEN))
        .body(request)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Meal.class)
        .returnResult()
        .getResponseBody();

    assertNotNull(response);
    assertEquals(request.getTitle(), response.getTitle());
    assertEquals(request.getMealTime(), response.getMealTime());
    assertEquals(1, response.getFoods().size());
  }

  @Test
  void shouldDeleteMeal() {
    restTestClient.delete()
        .uri("/patients/{patientId}/menu/meals/{mealId}", PATIENT_ID, MEAL1_ID)
        .headers(h -> h.setBearerAuth(TOKEN))
        .exchange()
        .expectStatus().isNoContent();
  }
}
