package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;

public interface FieldService {

    void createField(CreateFieldReq createFieldReq, Long userId);
}
