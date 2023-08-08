package com.dnd.Exercise.domain.teamBattleEntry.entity;

import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.domain.team.entity.Team;
import com.dnd.Exercise.domain.teamBattle.entity.TeamBattle;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class TeamBattleEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_battle_entry_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "team_id")
    private Team receiver;
}
