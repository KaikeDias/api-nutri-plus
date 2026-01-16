package com.dias.nutri_plus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "patients")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class Patient extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String cpf;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String phone;

  private Double height;

  private Double weight;

  @Column(nullable = false, unique = true, length = 36)
  private String keycloakUserId;

  @JsonIgnore
  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
  private Menu menu;
}
