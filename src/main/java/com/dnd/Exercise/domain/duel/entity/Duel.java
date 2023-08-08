package com.dnd.Exercise.domain.duel.entity;

import com.dnd.Exercise.domain.team.entity.Goal;
import com.dnd.Exercise.domain.team.entity.Period;
import com.dnd.Exercise.domain.team.entity.Strength;
import com.dnd.Exercise.domain.teamBattle.entity.BattleStatus;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Duel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "duel_id")
    private Long id;

    private String name;

    private String profile_img;

    private String description;

    @Enumerated(EnumType.STRING)
    private Strength strength;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private String rule;

    @Enumerated(EnumType.STRING)
    private BattleStatus battleStatus;

    @Enumerated(EnumType.STRING)
    private Period period;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;


}
