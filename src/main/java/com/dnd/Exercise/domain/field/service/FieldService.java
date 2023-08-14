package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import org.springframework.data.domain.Pageable;

public interface FieldService {

    void createField(CreateFieldReq createFieldReq, Long userId);

    FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond, Pageable pageable);
}
