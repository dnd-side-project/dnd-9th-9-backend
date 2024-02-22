package com.dnd.Exercise.domain.BattleEntry.repository;

import com.dnd.Exercise.domain.BattleEntry.entity.BattleEntry;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BattleEntryRepository extends JpaRepository<BattleEntry, Long>{

    @EntityGraph(attributePaths = {"entrantUser", "entrantField", "hostField"})
    Optional<BattleEntry> findById(Long id);

    @EntityGraph(attributePaths = "hostField")
    Page<BattleEntry> findByEntrantField(Field field, Pageable pageable);

    @EntityGraph(attributePaths = "entrantField")
    Page<BattleEntry> findByHostField(Field field, Pageable pageable);

    void deleteAllByEntrantField(Field field);

    void deleteAllByHostField(Field field);

    void deleteAllByEntrantFieldOrHostField(Field entrantField, Field hostField);

    Boolean existsByEntrantFieldAndHostField(Field entrant, Field host);
}
