package com.dnd.Exercise.domain.user.entity;

import javax.persistence.*;

import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.global.common.BaseEntity;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder
    public User(String uid, String password, String phoneNum, String name, SkillLevel skillLevel, LoginType loginType) {
        this.uid = uid;
        this.password = password;
        this.phoneNum = phoneNum;
        this.name = name;
        this.skillLevel = skillLevel;
        this.loginType = loginType;
    }
}
