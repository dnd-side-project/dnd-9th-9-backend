package com.dnd.Exercise.domain.Badge.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long id;

    private String name;

    private String description;
}
