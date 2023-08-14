package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FieldRepositoryCustom {
    Page<Field> findAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond, Pageable pageable);
}
