package com.dias.nutri_plus.repositories;

import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
  Optional<Menu> findByPatient(Patient patient);
}
