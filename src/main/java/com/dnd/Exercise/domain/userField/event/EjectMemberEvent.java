package com.dnd.Exercise.domain.userField.event;

import com.dnd.Exercise.domain.field.entity.Field;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EjectMemberEvent {
    private Field field;
    private List<Long> memberIds;

    public static EjectMemberEvent from(Field field, List<Long> memberIds){
        return new EjectMemberEvent(field, memberIds);
    }
}
