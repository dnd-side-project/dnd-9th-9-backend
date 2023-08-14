package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {

}
