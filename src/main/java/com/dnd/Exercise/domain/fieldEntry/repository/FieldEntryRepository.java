package com.dnd.Exercise.domain.fieldEntry.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldEntryRepository extends JpaRepository<FieldEntry, Long> {

    Boolean existsByEntrantUserAndHostField(User user, Field field);

    Boolean existsByEntrantFieldAndHostField(Field entrant, Field host);
}
