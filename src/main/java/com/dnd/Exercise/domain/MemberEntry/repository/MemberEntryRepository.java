package com.dnd.Exercise.domain.MemberEntry.repository;

import com.dnd.Exercise.domain.MemberEntry.entity.MemberEntry;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

public interface MemberEntryRepository extends CrudRepository<MemberEntry, Long> {

    boolean existsByEntrantUserAndHostField(User entrantUser, Field hostField);

    void deleteAllByEntrantUser(User entrantUser);

    void deleteAllByHostField(Field hostField);

    @EntityGraph(attributePaths = "entrantUser")
    Page<MemberEntry> findAllByHostField(Field field, Pageable pageable);

    @EntityGraph(attributePaths = "hostField")
    Page<MemberEntry> findAllByEntrantUser(User user, Pageable pageable);
}
