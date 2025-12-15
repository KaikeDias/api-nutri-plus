package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Food;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.mappers.MealMapper;
import com.dias.nutri_plus.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {
  private final MenuRepository menuRepository;
  private final PatientService patientService;
  private final MealMapper mealMapper;

  public Menu getOrCreateMenu(Patient patient) {
    Optional<Menu> menu = menuRepository.findByPatient(patient);

    if (menu.isPresent()) {
      return menu.get();
    }

    Menu newMenu = new Menu();
    newMenu.setPatient(patient);

    return menuRepository.save(newMenu);
  }

  public Menu addMeal(UUID patientID, MealRequestDTO mealDto) {
    Patient patient = patientService.findById(patientID);
    Menu menu = getOrCreateMenu(patient);

    Meal newMeal = mealMapper.requestToEntity(mealDto);

// set meal → menu
    newMeal.setMenu(menu);

// 🔴 ESSENCIAL: set meal em TODOS os foods
    bindMealRecursively(newMeal, newMeal.getFoods());

    menu.getMeals().add(newMeal);

    return menuRepository.save(menu);

  }

  private void bindMealRecursively(Meal meal, List<Food> foods) {
    if (foods == null) return;

    for (Food food : foods) {
      food.setMeal(meal);

      if (food.getSubstitutions() != null) {
        bindMealRecursively(meal, food.getSubstitutions());
      }
    }
  }



}
