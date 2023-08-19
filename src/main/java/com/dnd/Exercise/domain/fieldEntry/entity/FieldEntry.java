package com.dnd.Exercise.domain.fieldEntry.entity;

import static javax.persistence.FetchType.*;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FieldEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_entry_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_user_id")
    private User entrantUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_field_id")
    private Field entrantField;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host_field_id")
    private Field hostField;

    @Builder
    public FieldEntry(FieldType fieldType, User entrantUser, Field entrantField, Field hostField) {
        this.fieldType = fieldType;
        this.entrantUser = entrantUser;
        this.entrantField = entrantField;
        this.hostField = hostField;
    }
}
