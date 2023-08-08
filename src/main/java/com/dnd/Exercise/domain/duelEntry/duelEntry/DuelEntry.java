package com.dnd.Exercise.domain.duelEntry.duelEntry;

import static javax.persistence.FetchType.LAZY;

import com.dnd.Exercise.domain.user.entity.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class DuelEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "duel_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User sender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User receiver;
}
