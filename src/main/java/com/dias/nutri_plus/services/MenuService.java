package com.dias.nutri_plus.services;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Food;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.entities.Patient;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.exceptions.NotFoundError;
import com.dias.nutri_plus.mappers.FoodMapper;
import com.dias.nutri_plus.mappers.MealMapper;
import com.dias.nutri_plus.repositories.MealRepository;
import com.dias.nutri_plus.repositories.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dias.nutri_plus.utils.validators.MealValidator.validateMealTitle;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final PatientService patientService;
    private final MealRepository mealRepository;
    private final MealMapper mealMapper;
    private final FoodMapper foodMapper;

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

        validateMealTitle(mealRepository, menu.getId(), mealDto.getTitle());

        Meal newMeal = mealMapper.requestToEntity(mealDto);
        newMeal.setMenu(menu);

        menu.getMeals().add(newMeal);

        return menuRepository.save(menu);
    }

    public Meal updateMeal(UUID mealId, MealRequestDTO mealDto) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new NotFoundError("Meal not found"));

        meal.setTitle(mealDto.getTitle());
        meal.setMealTime(mealDto.getMealTime());

        List<Food> foods = foodMapper.requestListToEntityList(mealDto.getFoods());
        meal.getFoods().clear();
        meal.getFoods().addAll(foods);

        return mealRepository.save(meal);
    }

    public Menu findMenuByPatientId(UUID patientId) {
      Patient patient = patientService.findById(patientId);

      return menuRepository.findByPatient(patient).orElseThrow(() -> new NotFoundError("Menu not found"));
    }

    public Page<Meal> findMeals(UUID patientId, Pageable pageable) {
        Menu menu = findMenuByPatientId(patientId);

        return mealRepository.findAllByMenuId(menu.getId(), pageable);
    }

    public void deleteMeal(UUID mealId) {
        mealRepository.deleteById(mealId);
    }
}
