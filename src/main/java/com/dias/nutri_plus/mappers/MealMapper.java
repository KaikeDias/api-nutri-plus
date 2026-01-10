package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Food;
import com.dias.nutri_plus.entities.Meal;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = FoodMapper.class
)
public interface MealMapper {

    Meal requestToEntity(MealRequestDTO dto);

    @AfterMapping
    default void linkMeal(@MappingTarget Meal meal) {
        if (meal.getFoods() != null) {
            for (Food food : meal.getFoods()) {
                food.setMeal(meal);

                if (food.getSubstitutions() != null) {
                    for (Food sub : food.getSubstitutions()) {
                        sub.setMeal(meal);
                    }
                }
            }
        }
    }
}
