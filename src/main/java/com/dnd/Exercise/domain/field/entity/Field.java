package com.dnd.Exercise.domain.field.entity;

import static javax.persistence.FetchType.LAZY;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long id;

    private String name;

    private String profileImg;

    @Enumerated(EnumType.STRING)
    private Strength strength;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private String rule;

    private int maxSize;

    private int currentSize;

    @Enumerated(EnumType.STRING)
    private Period period;

    private String description;

    private Long leaderId;

    @Enumerated(EnumType.STRING)
    private FieldStatus fieldStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "opponent_id")
    private Field opponent;
}
