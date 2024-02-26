package com.dnd.Exercise.domain.userField.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.AccumulatedActivityDto;
import com.dnd.Exercise.domain.userField.dto.AccumulatedExerciseDto;
import com.dnd.Exercise.domain.userField.entity.UserField;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserFieldRepository extends JpaRepository<UserField, Long>, UserFieldRepositoryCustom {

     @Query(value =
             "SELECT NEW com.dnd.Exercise.domain.userField.dto.AccumulatedActivityDto( "
                     + "u.name, SUM(ar.burnedCalorie), "
                     + "SUM(CASE WHEN ar.isGoalAchieved = TRUE THEN 1 ELSE 0 END)) "
                     + "FROM ActivityRing ar "
                     + "JOIN ar.user u "
                     + "WHERE ar.date BETWEEN :startDate AND CURRENT_DATE "
                     + "AND u.id IN :userIds "
                     + "GROUP BY u"
     )
    List<AccumulatedActivityDto> findAccumulatedActivityValues(LocalDate startDate, List<Long> userIds);


    @Query(value =
            "SELECT NEW com.dnd.Exercise.domain.userField.dto.AccumulatedExerciseDto( "
                    + "u.name, SUM(e.durationMinute), COUNT(e)) "
                    + "FROM Exercise e "
                    + "JOIN e.user u "
                    + "WHERE e.date BETWEEN :startDate AND CURRENT_DATE "
                    + "AND u.id IN :userIds "
                    + "GROUP BY u"
    )
    List<AccumulatedExerciseDto> findAccumulatedExerciseValues(LocalDate startDate, List<Long> userIds);


     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.user "
                     + "where uf.field.id = :id"
     )
     List<UserField> findAllByFieldId(@Param("id") Long id);

    @Query(value =
            "select uf from UserField uf "
                    + "join fetch uf.user "
                    + "where uf.field.id in :ids"
    )
    List<UserField> findAllByFieldIdIn(@Param("ids") List<Long> ids);

     @Query(value =
             "select uf from UserField uf "
                     + "join fetch uf.field "
                     + "where uf.user = :user "
                     + "and uf.field.fieldStatus in :fieldStatuses "
                     + "and uf.field.fieldType in :fieldTypes"
     )
     List<UserField> findByUserAndStatusInAndTypeIn(@Param("user") User user,
             @Param("fieldStatuses") List<FieldStatus> fieldStatuses,
             @Param("fieldTypes") List<FieldType> fieldTypes);

    @Query(value =
            "select uf from UserField uf "
                    + "join fetch uf.field "
                    + "where uf.user = :user "
                    + "and uf.field.fieldStatus in :fieldStatuses "
                    + "and uf.field.fieldType = :fieldType"
    )
    List<UserField> findByUserAndStatusInAndType(@Param("user") User user,
            @Param("fieldStatuses") List<FieldStatus> fieldStatuses,
            @Param("fieldType") FieldType fieldTypes);

    @Query(value =
            "select uf from UserField uf "
                    + "join fetch uf.field "
                    + "where uf.user = :user "
                    + "and uf.field.fieldStatus = :fieldStatus "
                    + "and uf.field.fieldType = :fieldType"
    )
    Optional<UserField> findByUserAndStatusAndType(@Param("user") User user,
            @Param("fieldStatus") FieldStatus fieldStatus,
            @Param("fieldType") FieldType fieldType);

    @Query(value =
            "select uf from UserField uf "
                    + "join fetch uf.field "
                    + "where uf.user = :user "
                    + "and uf.field.fieldStatus = :fieldStatus"
    )
    List<UserField> findByUserAndStatus(@Param("user") User user,
            @Param("fieldStatus") FieldStatus fieldStatus);

    @Query(value =
            "select uf from UserField uf "
                    + "join fetch uf.field "
                    + "where uf.user = :user "
                    + "and uf.field.fieldStatus in :fieldStatuses"
    )
    List<UserField> findByUserAndStatusIn(@Param("user") User user,
            @Param("fieldStatuses") List<FieldStatus> fieldStatuses);

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

    @EntityGraph(attributePaths = "field")
    List<UserField> findAllByUser(User user);

    Boolean existsByFieldAndUser(Field field, User user);

    void deleteAllByField(Field field);

    void deleteAllByFieldAndUserIdIn(Field field, List<Long> targetUserIds);

    void deleteByFieldAndUser(Field field, User user);
}
