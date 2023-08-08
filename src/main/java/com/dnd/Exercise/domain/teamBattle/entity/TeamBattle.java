package com.dnd.Exercise.domain.teamBattle.entity;

import com.dnd.Exercise.domain.team.entity.Period;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
public class TeamBattle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_battle_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private BattleStatus battleStatus;

    @Enumerated(EnumType.STRING)
    private Period period;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;
}
