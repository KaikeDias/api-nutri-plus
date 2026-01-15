package com.dias.nutri_plus.entities;

import com.dias.nutri_plus.enums.Unit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "foods")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class Food extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String quantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Column(nullable = false)
    private String homeQuantity;

    @Column(nullable = false)
    private String homeUnit;

    @OneToMany(mappedBy = "parentFood", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Food> substitutions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_food_id")
    @JsonIgnore
    private Food parentFood;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;
}
