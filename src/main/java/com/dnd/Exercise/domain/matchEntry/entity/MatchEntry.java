package com.dnd.Exercise.domain.matchEntry.entity;

import static javax.persistence.FetchType.*;

import com.dnd.Exercise.domain.match.entity.Match;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.user.entity.User;
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
public class MatchEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_entry_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_user_id")
    private User entrantUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_match_id")
    private Match entrantMatch;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host_match_id")
    private Match hostMatch;
}
