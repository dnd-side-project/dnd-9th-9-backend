package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldStatus;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFieldRepository extends JpaRepository<UserField, Long> {


     Boolean existsByFieldAndUser(Field field, User user);

     void deleteAllByField(Field field);

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.field "
                     + "where uf.user.id = :id and uf.field.fieldType = :type "
                     + "and uf.field.fieldStatus in :statusList"
     )
     Optional<UserField> findMyFieldByTypeAndStatus(@Param("id") Long id,
             @Param("type") FieldType fieldType, @Param("statusList") List<FieldStatus> statusList);
}
