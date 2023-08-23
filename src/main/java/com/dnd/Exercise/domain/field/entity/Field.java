package com.dnd.Exercise.domain.field.entity;

import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.global.common.BaseEntity;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Field extends BaseEntity {
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

    @Builder
    public Field(String name, String profileImg, Strength strength, Goal goal,
            String rule, int maxSize, Period period,
            String description, Long leaderId, FieldType fieldType,
            SkillLevel skillLevel) {
        this.name = name;
        this.profileImg = profileImg;
        this.strength = strength;
        this.goal = goal;
        this.rule = rule;
        this.maxSize = maxSize;
        this.currentSize = 1;
        this.period = period;
        this.description = description;
        this.leaderId = leaderId;
        this.fieldStatus = FieldStatus.RECRUITING;
        this.startDate = null;
        this.endDate = null;
        this.fieldType = fieldType;
        this.skillLevel = skillLevel;
        this.opponent = null;
    }

    public void removeOpponent(){
        this.opponent = null;
    }

    public void changeOpponent(Field field){
        this.opponent = field;
        field.opponent = this;
    }

    public void addMember(){
        this.currentSize += 1;
    }

    public void changeProfileImg(String imgUrl){
        this.profileImg = imgUrl;
    }
}
