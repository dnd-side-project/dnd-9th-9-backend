package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.FieldStatus;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.Period;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long>, FieldRepositoryCustom {

    @Query(value =
            "select f from Field f "
                    + "where f.fieldStatus = :fieldStatus and f.fieldType = :type "
                    + "and f.period = :period"
    )
    List<Field> findAllByCond(@Param("fieldStatus") FieldStatus fieldStatus,
                        @Param("type") FieldType fieldType,
                        @Param("period") Period period);

    Optional<Field> findByIdAndFieldType(Long id, FieldType fieldType);
}
