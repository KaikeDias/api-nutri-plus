package com.dias.nutri_plus.utils.validators;

import com.dias.nutri_plus.entities.Meal;
import com.dias.nutri_plus.exceptions.ConflictError;
import com.dias.nutri_plus.repositories.MealRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MealValidatorTest {

    @Mock
    private MealRepository mealRepository;

    @Test
    void shouldNotThrowExceptionWhenMealTitleDoesNotExist() {
        UUID menuId = UUID.randomUUID();
        String title = "Breakfast";

        when(mealRepository.findByMenuIdAndTitle(menuId, title))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() ->
                MealValidator.validateMealTitle(mealRepository, menuId, title)
        );
    }

    @Test
    void shouldThrowConflictErrorWhenMealTitleAlreadyExists() {
        UUID menuId = UUID.randomUUID();
        String title = "Lunch";

        when(mealRepository.findByMenuIdAndTitle(menuId, title))
                .thenReturn(Optional.of(new Meal()));

        ConflictError exception = assertThrows(
                ConflictError.class,
                () -> MealValidator.validateMealTitle(mealRepository, menuId, title)
        );

        assertEquals(
                "A meal with this title already exists. Please choose another title.",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowExceptionWhenInstantiatingUtilityClass() throws Exception {
        Constructor<MealValidator> constructor =
                MealValidator.class.getDeclaredConstructor();

        constructor.setAccessible(true);

        assertThrows(
                InvocationTargetException.class,
                constructor::newInstance
        );
    }
}
