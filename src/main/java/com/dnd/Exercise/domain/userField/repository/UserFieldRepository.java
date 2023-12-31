package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.entity.UserField;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFieldRepository extends JpaRepository<UserField, Long>, UserFieldRepositoryCustom {


     Boolean existsByFieldAndUser(Field field, User user);

     void deleteAllByField(Field field);


     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.user "
                     + "where uf.field.id = :id"
     )
     List<UserField> findAllByField(@Param("id") Long id);


     @EntityGraph(attributePaths = "field")
     List<UserField> findAllByUser(User user);

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.field "
                     + "where uf.user = :user "
                     + "and uf.field.fieldStatus in :fieldStatuses "
                     + "and uf.field.fieldType in :fieldTypes"
     )
     List<UserField> findByUserAndStatusInAndType(@Param("user") User user,
             @Param("fieldStatuses") List<FieldStatus> fieldStatuses,
             @Param("fieldTypes") List<FieldType> fieldTypes);


    void deleteAllByFieldAndUserIdIn(Field field, List<Long> targetUserIds);

    void deleteByFieldAndUser(Field field, User user);

    @Query("select uf " +
            "from UserField uf join fetch uf.field " +
            "where uf.user = :user " +
            "and uf.field.fieldStatus = :fieldStatus")
    List<UserField> findAllByUserAndFieldStatus(User user, FieldStatus fieldStatus);

    @Query("select uf " +
            "from UserField uf join fetch uf.field " +
            "where uf.user = :user " +
                "and uf.field.fieldStatus = 'RECRUITING' " +
                "and uf.field.opponent = null")
    List<UserField> findAllBeforeProgressFieldByUser(User user);

    @Query("select count(uf) " +
            "from UserField uf " +
            "where uf.user.id = :userId " +
                "and uf.field.fieldStatus = 'COMPLETED'")
    int countAllCompletedFieldsByUserId(long userId);

    @Query("select count(uf) " +
            "from UserField uf " +
            "where uf.user.id = :userId " +
                "and uf.field.fieldType in :fieldType " +
                "and uf.field.fieldStatus = 'COMPLETED'")
    int countCompletedFieldsByUserIdAndFieldType(long userId, List<FieldType> fieldType);
}
