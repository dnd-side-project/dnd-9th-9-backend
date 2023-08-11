package com.dnd.Exercise.domain.exercise.entity;

import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Exercise extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Sports sports;

    private LocalDate exerciseDate;

    private int durationMinute;

    private int burnedCalorie;

    private String memoImg;

    private String memoContent;

    private boolean isMemoPublic;

    @Enumerated(EnumType.STRING)
    private RecordProvider recordProvider;

    private String appleUid;
}
