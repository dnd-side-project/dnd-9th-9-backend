package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long>, FieldRepositoryCustom {

}
