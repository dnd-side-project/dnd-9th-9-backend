package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserFieldRepositoryCustom {

    Page<UserField> findCompletedFieldByUserAndType(User user, FieldType fieldType, Pageable pageable);
}
