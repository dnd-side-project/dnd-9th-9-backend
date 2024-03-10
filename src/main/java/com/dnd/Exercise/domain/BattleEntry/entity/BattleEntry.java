package com.dnd.Exercise.domain.BattleEntry.entity;

import static javax.persistence.FetchType.*;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BattleEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_entry_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_field_id")
    private Field entrantField;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host_field_id")
    private Field hostField;

    @Builder
    public BattleEntry(Field entrantField, Field hostField) {
        this.entrantField = entrantField;
        this.hostField = hostField;
    }

    public static BattleEntry from(Field entrantField, Field hostField){
        return BattleEntry.builder()
                .entrantField(entrantField)
                .hostField(hostField)
                .build();
    }
}
