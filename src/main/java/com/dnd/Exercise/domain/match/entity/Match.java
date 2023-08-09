package com.dnd.Exercise.domain.match.entity;

import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.domain.notification.entity.MatchType;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    private String name;

    private String profile_img;

    @Enumerated(EnumType.STRING)
    private Strength strength;

    @Enumerated(EnumType.STRING)
    private Goal goal;

    private String rule;

    private int maxSize;

    private int currentSize;

    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    private Period period;

    private String description;

    private Long leaderId;

    @Enumerated(EnumType.STRING)
    private MatchStatus MatchStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "opponent_id")
    private Match opponent;
}
