package com.dnd.Exercise.domain.user.entity;

import com.dnd.Exercise.domain.fcmToken.entity.FcmToken;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.global.common.BaseEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String oauthId;

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

    private double height;

    private double weight;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;

    private int calorieGoal;

    private int teamworkRate;

    private Boolean isNotificationAgreed;

    private Boolean isAppleLinked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FcmToken> fcmTokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }


    @Override
    public String getUsername() {
        return id.toString();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public User(String uid, String password, String phoneNum, String name, SkillLevel skillLevel, LoginType loginType, Boolean isAppleLinked, Boolean isNotificationAgreed, String oauthId, String email) {
        this.uid = uid;
        this.password = password;
        this.phoneNum = phoneNum;
        this.name = name;
        this.skillLevel = skillLevel;
        this.loginType = loginType;
        this.isAppleLinked = isAppleLinked;
        this.isNotificationAgreed = isNotificationAgreed;
        this.oauthId = oauthId;
        this.email = email;
    }

    public void addToken(FcmToken fcmToken) {
        fcmTokens.add(fcmToken);
        fcmToken.addUser(this);
    }

    public void updateSkillLevel(SkillLevel skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void updateProfileImgUrl(String imgUrl) {
        this.profileImg = imgUrl;
    }

    public void updateIsAppleLinked(Boolean isAppleLinked) {
        this.isAppleLinked = isAppleLinked;
    }

    public void updateIsNotificationAgreed(Boolean isNotificationAgreed) {
        this.isNotificationAgreed = isNotificationAgreed;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateTeamworkRate(int teamworkRate) { this.teamworkRate = teamworkRate; }
}
