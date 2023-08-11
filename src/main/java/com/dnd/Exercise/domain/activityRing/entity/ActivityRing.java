package com.dnd.Exercise.domain.activityRing.entity;

import com.dnd.Exercise.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class ActivityRing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_ring_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate date;

    private int burnedCalorie;

    private Boolean isGoalAchieved;
}
