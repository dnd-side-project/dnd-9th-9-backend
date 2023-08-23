package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldStatus;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFieldRepository extends JpaRepository<UserField, Long>, UserFieldRepositoryCustom {


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

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.user "
                     + "where uf.field.id = :id"
     )
     List<UserField> findAllByField(@Param("id") Long id);

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.field "
                     + "where uf.user = :user "
                     + "and uf.field.fieldStatus in :fieldStatuses "
                     + "and uf.field.fieldType = :fieldType"
     )
     Optional<UserField> findByUserAndStatusAndType(@Param("user") User user,
             @Param("fieldStatuses") List<FieldStatus> fieldStatuses, @Param("fieldType") FieldType fieldType);

     @EntityGraph(attributePaths = "field")
     List<UserField> findAllByUser(User user);

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.field "
                     + "where uf.user = :user "
                     + "and uf.field.fieldStatus in :fieldStatuses "
                     + "and uf.field.fieldType in :fieldTypes"
     )
     List<UserField> findByUserAndStatusIn(@Param("user") User user,
             @Param("fieldStatuses") List<FieldStatus> fieldStatuses,
             @Param("fieldTypes") List<FieldType> fieldTypes);


}
