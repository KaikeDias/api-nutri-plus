package com.dias.nutri_plus.repositories;

import com.dias.nutri_plus.entities.Meal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MealRepository extends JpaRepository<Meal, UUID> {

    Page<Meal> findAllByMenuId(UUID menuId, Pageable pageable);
}
