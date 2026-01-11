package com.dias.nutri_plus.utils.validators;


import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.repositories.MealRepository;

import java.util.UUID;

public final class MealValidator {

    private MealValidator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void validateMealTitle(MealRepository mealRepository, UUID menuID, String title) {
        if(mealRepository.findByMenuIdAndTitle(menuID, title).isPresent()) {
            throw new ConflictError("A meal with this title already exists. Please choose another title.");
        }
    }
}
