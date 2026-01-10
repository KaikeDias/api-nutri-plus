package com.dias.nutri_plus.mappers;

import com.dias.nutri_plus.dtos.menu.BaseFoodRequestDTO;
import com.dias.nutri_plus.dtos.menu.FoodRequestDTO;
import com.dias.nutri_plus.entities.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FoodMapper {

    @Mapping(target = "name", source = "food.name")
    @Mapping(target = "quantity", source = "food.quantity")
    @Mapping(target = "unit", source = "food.unit")
    @Mapping(target = "homeQuantity", source = "food.homeQuantity")
    @Mapping(target = "homeUnit", source = "food.homeUnit")
    @Mapping(target = "substitutions", source = "substitutions")
    Food requestToEntity(FoodRequestDTO dto);

    List<Food> requestListToEntityList(List<FoodRequestDTO> dtos);

    Food substitutionToEntity(BaseFoodRequestDTO dto);
}

