package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Meal;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = FoodMapper.class
)
public interface MealMapper {

  Meal requestToEntity(MealRequestDTO dto);
}
