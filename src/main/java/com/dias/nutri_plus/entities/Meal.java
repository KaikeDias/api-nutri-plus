package com.dias.nutri_plus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meals")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Meal extends AuditableEntity{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private LocalTime mealTime;

  @OneToMany( mappedBy = "meal", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Food> foods;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "menu_id", nullable = false)
  private Menu menu;
}
