package com.dnd.Exercise.domain.teamworkRate.entity;

import com.dnd.Exercise.domain.field.entity.Field;
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
    @JoinColumn(name = "field_id")
    private Field field;

    private int rate;
}
