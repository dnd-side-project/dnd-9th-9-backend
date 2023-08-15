package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFieldRepository extends JpaRepository<UserField, Long> {


     Boolean existsByFieldAndUser(Field field, User user);

     void deleteAllByField(Field field);
}
