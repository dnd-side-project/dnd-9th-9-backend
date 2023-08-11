package com.dnd.Exercise.domain.teamworkRate.entity;

import com.dnd.Exercise.domain.match.entity.Match;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class TeamworkRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamwork_rate_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "submit_user_id")
    private User submitUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    private int rate;
}
