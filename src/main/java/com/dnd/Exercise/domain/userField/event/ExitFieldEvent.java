package com.dnd.Exercise.domain.userField.event;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExitFieldEvent {
    private User user;
    private Field field;

    public static ExitFieldEvent from(User user, Field field){
        return new ExitFieldEvent(user ,field);
    }
}
