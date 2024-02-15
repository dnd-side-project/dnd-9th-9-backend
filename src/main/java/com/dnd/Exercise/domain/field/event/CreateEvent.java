package com.dnd.Exercise.domain.field.event;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateEvent {
    private final User creator;
    private final Field field;

    static public CreateEvent newEvent(User creator, Field field){
        return new CreateEvent(creator, field);
    }
}
