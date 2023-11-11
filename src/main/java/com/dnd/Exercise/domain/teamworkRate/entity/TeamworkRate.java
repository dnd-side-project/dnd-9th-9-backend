package com.dnd.Exercise.domain.teamworkRate.entity;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public TeamworkRate(User user, Field field, int rate) {
        this.submitUser = user;
        this.field = field;
        this.rate = rate;
    }
}
