package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.menu.BaseFoodRequestDTO;
import com.dias.nutri_plus.dtos.menu.FoodRequestDTO;
import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.enums.Unit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        MealMapperImpl.class,
        FoodMapperImpl.class
})
class MealMapperTest {

    @Autowired
    private MealMapper mealMapper;

    @Test
    void shouldLinkMealToFoods() {
        BaseFoodRequestDTO base = new BaseFoodRequestDTO();
        base.setName("Rice");
        base.setQuantity("100");
        base.setUnit(Unit.GRAM);
        base.setHomeQuantity("2");
        base.setHomeUnit("spoons");

        FoodRequestDTO food = new FoodRequestDTO();
        food.setFood(base);

        MealRequestDTO dto = new MealRequestDTO();
        dto.setTitle("Lunch");
        dto.setFoods(List.of(food));

        Meal meal = mealMapper.requestToEntity(dto);

        assertNotNull(meal);
        assertEquals("Lunch", meal.getTitle());
        assertEquals(meal, meal.getFoods().get(0).getMeal());
    }
}

