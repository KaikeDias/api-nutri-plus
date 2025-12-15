package com.dias.nutri_plus.controllers;

import com.dias.nutri_plus.dtos.menu.MealRequestDTO;
import com.dias.nutri_plus.entities.Menu;
import com.dias.nutri_plus.services.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/patients/{patientId}/menu")
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/add-meal")
  @ResponseStatus(code = HttpStatus.CREATED)
  public ResponseEntity<Menu> addMeal(@PathVariable UUID patientId, @RequestBody MealRequestDTO request) {
    Menu menu = menuService.addMeal(patientId, request);

    URI uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(menu.getId())
        .toUri();

    return ResponseEntity.created(uri).body(menu);
  }

}
