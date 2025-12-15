package com.dias.nutri_plus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "foods")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Food extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String quantity;

  @Column(nullable = false)
  private String unit;

  @Column(nullable = false)
  private String homeQuantity;

  @Column(nullable = false)
  private String homeUnit;

  private List<Food> substitutions;
}
