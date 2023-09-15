package com.dnd.Exercise.domain.field.repository;

import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.entity.Field;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FieldRepositoryCustom {
    List<Field> findAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond);

    Long countAllFieldsWithFilter(FindAllFieldsCond findAllFieldsCond);
}
