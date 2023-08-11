package com.dnd.Exercise.domain.bookmark.entity;

import com.dnd.Exercise.domain.sports.entity.Sports;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Sports sports;
}
