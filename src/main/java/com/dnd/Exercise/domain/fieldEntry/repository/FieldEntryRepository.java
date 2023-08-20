package com.dnd.Exercise.domain.fieldEntry.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.fieldEntry.entity.FieldEntry;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldEntryRepository extends JpaRepository<FieldEntry, Long>, FieldEntryRepositoryCustom {

    Boolean existsByEntrantUserAndHostField(User user, Field field);

    Boolean existsByEntrantFieldAndHostField(Field entrant, Field host);

    @EntityGraph(attributePaths = {"entrantUser", "entrantField", "hostField"})
    Optional<FieldEntry> findById(Long id);

    void deleteAllByEntrantUser(User user);

    void deleteAllByEntrantField(Field field);
}
