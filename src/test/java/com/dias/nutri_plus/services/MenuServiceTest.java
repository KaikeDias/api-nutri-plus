package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.FoodMapper;
import com.dias.nutri_plus.mappers.MealMapper;
import com.dias.nutri_plus.repositories.MealRepository;
import com.dias.nutri_plus.repositories.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private PatientService patientService;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private MealMapper mealMapper;

    @Mock
    private FoodMapper foodMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private MenuService menuService;

    private static final String CURRENT_USER_ID = "user-123";

    @Test
    void addMealShouldAddMealSuccessfully() {
        UUID patientId = UUID.randomUUID();
        UUID menuId = UUID.randomUUID();

        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setKeycloakUserId(CURRENT_USER_ID);

        Menu menu = new Menu();
        menu.setId(menuId);
        menu.setPatient(patient);
        menu.setMeals(new java.util.ArrayList<>());

        MealRequestDTO mealDto = new MealRequestDTO();
        mealDto.setTitle("Breakfast");

        Meal mealEntity = new Meal();

        lenient().when(authService.getCurrentUserSub()).thenReturn(CURRENT_USER_ID);
        when(patientService.findById(patientId)).thenReturn(patient);
        when(menuRepository.findByPatient(patient)).thenReturn(Optional.of(menu));
        when(mealMapper.requestToEntity(mealDto)).thenReturn(mealEntity);
        when(menuRepository.save(menu)).thenReturn(menu);

        Menu result = menuService.addMeal(patientId, mealDto);

        assertNotNull(result);
        assertEquals(1, result.getMeals().size());
        verify(menuRepository).save(menu);
    }

    @Test
    void updateMealShouldUpdateMealSuccessfully() {
        UUID mealId = UUID.randomUUID();

        Patient patient = new Patient();
        patient.setKeycloakUserId(CURRENT_USER_ID);

        Menu menu = new Menu();
        menu.setPatient(patient);

        Meal meal = new Meal();
        meal.setId(mealId);
        meal.setMenu(menu);
        meal.setFoods(new java.util.ArrayList<>());

        MealRequestDTO dto = new MealRequestDTO();
        dto.setTitle("Lunch");
        dto.setMealTime(LocalTime.parse("12:00"));
        dto.setFoods(List.of());

        lenient().when(authService.getCurrentUserSub()).thenReturn(CURRENT_USER_ID);
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));
        when(foodMapper.requestListToEntityList(dto.getFoods())).thenReturn(List.of());
        when(mealRepository.save(meal)).thenReturn(meal);

        Meal updatedMeal = menuService.updateMeal(mealId, dto);

        assertNotNull(updatedMeal);
        assertEquals("Lunch", updatedMeal.getTitle());
        assertEquals(LocalTime.parse("12:00"), updatedMeal.getMealTime());
        verify(mealRepository).save(meal);
    }

    @Test
    void updateMealShouldThrowNotFoundErrorWhenMealBelongsToAnotherUser() {
        UUID mealId = UUID.randomUUID();

        Patient patient = new Patient();
        patient.setKeycloakUserId("another-user");

        Menu menu = new Menu();
        menu.setPatient(patient);

        Meal meal = new Meal();
        meal.setId(mealId);
        meal.setMenu(menu);
        meal.setFoods(new java.util.ArrayList<>());

        MealRequestDTO dto = new MealRequestDTO();
        dto.setTitle("Dinner");

        lenient().when(authService.getCurrentUserSub()).thenReturn(CURRENT_USER_ID);
        when(mealRepository.findById(mealId)).thenReturn(Optional.of(meal));

        NotFoundError exception = assertThrows(NotFoundError.class, () ->  menuService.updateMeal(mealId, dto));

        assertEquals("Meal not found for current user", exception.getMessage());
        verify(mealRepository, never()).save(any());
    }

    @Test
    void findMenuByPatientIdShouldReturnMenuWhenMenuExists() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setKeycloakUserId(CURRENT_USER_ID);

        Menu menu = new Menu();
        menu.setId(UUID.randomUUID());
        menu.setPatient(patient);

        lenient().when(authService.getCurrentUserSub()).thenReturn(CURRENT_USER_ID);
        when(patientService.findById(patientId)).thenReturn(patient);
        when(menuRepository.findByPatient(patient)).thenReturn(Optional.of(menu));

        Menu result = menuService.findMenuByPatientId(patientId);

        assertNotNull(result);
        assertEquals(menu.getId(), result.getId());
    }

    @Test
    void findMenuByPatientIdShouldThrowNotFoundErrorWhenMenuDoesNotExist() {
        UUID patientId = UUID.randomUUID();
        Patient patient = new Patient();
        patient.setId(patientId);
        patient.setKeycloakUserId(CURRENT_USER_ID);

        lenient().when(authService.getCurrentUserSub()).thenReturn(CURRENT_USER_ID);
        when(patientService.findById(patientId)).thenReturn(patient);
        when(menuRepository.findByPatient(patient)).thenReturn(Optional.empty());

        NotFoundError exception = assertThrows(NotFoundError.class, () -> menuService.findMenuByPatientId(patientId));

        assertEquals("Menu not found", exception.getMessage());
    }
}
