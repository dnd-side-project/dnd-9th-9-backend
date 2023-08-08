package com.dnd.Exercise.domain.team.entity;

import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.domain.teamBattle.entity.TeamBattle;
import com.dnd.Exercise.domain.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    private String profile_img;

    @Enumerated(EnumType.STRING)
    private Strength strength;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private Boolean isPublic;

    private String rule;

    private int teamSize;

    private Boolean isActive;

    private Period period;

    private String description;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User leader;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_battle_id")
    private TeamBattle teamBattle;
}
