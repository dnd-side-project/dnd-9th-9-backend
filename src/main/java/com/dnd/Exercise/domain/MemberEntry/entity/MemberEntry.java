package com.dnd.Exercise.domain.MemberEntry.entity;

import static com.dnd.Exercise.global.error.dto.ErrorCode.BAD_REQUEST;
import static javax.persistence.FetchType.*;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.BaseEntity;
import com.dnd.Exercise.global.error.exception.BusinessException;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class MemberEntry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_entry_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "entrant_user_id")
    private User entrantUser;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host_field_id")
    private Field hostField;

    @Builder
    public MemberEntry(User entrantUser, Field hostField) {
        this.entrantUser = entrantUser;
        this.hostField = hostField;
    }

    public static MemberEntry from(User entrantUser, Field hostField){
        return MemberEntry.builder()
                .entrantUser(entrantUser)
                .hostField(hostField)
                .build();
    }

    public void validateMyEntry(User user){
        if(!this.entrantUser.getId().equals(user.getId())) {
            throw new BusinessException(BAD_REQUEST);
        }
    }
}

