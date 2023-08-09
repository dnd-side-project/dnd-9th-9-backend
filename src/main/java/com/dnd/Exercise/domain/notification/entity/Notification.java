package com.dnd.Exercise.domain.notification.entity;

import static javax.persistence.FetchType.*;

import com.dnd.Exercise.domain.match.entity.Match;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content;

    private Boolean isRead;

    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "match_id")
    private Match match;
}
