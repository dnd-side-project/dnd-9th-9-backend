package com.dnd.Exercise.domain.sports.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SportsCalorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sports_calorie_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Sports sports;

    private int baseCalorie;
}
