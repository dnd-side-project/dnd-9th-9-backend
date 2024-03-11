package com.dnd.Exercise.domain.MemberEntry.event;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AcceptMemberEvent {
    private User entrantUser;
    private Field hostField;

    public static AcceptMemberEvent from(User entrantUser, Field hostField){
        return new AcceptMemberEvent(entrantUser, hostField);
    }
}
