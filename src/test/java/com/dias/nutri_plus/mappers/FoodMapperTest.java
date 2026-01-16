package com.dias.nutri_plus.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.dias.nutri_plus.dtos.menu.BaseFoodRequestDTO;
import com.dias.nutri_plus.dtos.menu.FoodRequestDTO;
import com.dias.nutri_plus.entities.Food;
import com.dias.nutri_plus.enums.Unit;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

class FoodMapperTest {

    private final FoodMapper foodMapper = Mappers.getMapper(FoodMapper.class);

    private BaseFoodRequestDTO baseFood() {
        BaseFoodRequestDTO dto = new BaseFoodRequestDTO();
        dto.setName("Chicken");
        dto.setQuantity("100");
        dto.setUnit(Unit.GRAM);
        dto.setHomeQuantity("1");
        dto.setHomeUnit("piece");
        return dto;
    }

    @Test
    void shouldMapFoodAndLinkSubstitutions() {
        BaseFoodRequestDTO substitution = baseFood();
        substitution.setName("Fish");

        FoodRequestDTO dto = new FoodRequestDTO();
        dto.setFood(baseFood());
        dto.setSubstitutions(List.of(substitution));

        Food food = foodMapper.requestToEntity(dto);

        assertNotNull(food);
        assertEquals("Chicken", food.getName());
        assertEquals("100", food.getQuantity());
        assertEquals(Unit.GRAM, food.getUnit());

        assertNotNull(food.getSubstitutions());
        assertEquals(1, food.getSubstitutions().size());

        Food sub = food.getSubstitutions().get(0);
        assertEquals("Fish", sub.getName());
        assertEquals(food, sub.getParentFood());
    }

    @Test
    void shouldMapWithoutSubstitutions() {
        FoodRequestDTO dto = new FoodRequestDTO();
        dto.setFood(baseFood());

        Food food = foodMapper.requestToEntity(dto);

        assertNotNull(food);
        assertEquals("Chicken", food.getName());
        assertNull(food.getSubstitutions());
    }
}
