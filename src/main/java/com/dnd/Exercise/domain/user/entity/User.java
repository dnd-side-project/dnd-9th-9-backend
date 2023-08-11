package com.dnd.Exercise.domain.user.entity;

import javax.persistence.*;

import com.dnd.Exercise.domain.match.entity.SkillLevel;
import com.dnd.Exercise.global.common.BaseEntity;
import lombok.Getter;

@Entity
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String uid;

    private String password;

    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String email;

    private String name;

    private String profileImg;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private int height;

    private int weight;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    private int calorieGoal;

    private int teamworkRate;

    private Boolean isNotificationAgreed;

    private Boolean isAppleLinked;
}
