package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface FieldService {

    void createField(CreateFieldReq createFieldReq, Long userId);

    FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond, Pageable pageable);

    FindFieldRes findField(Long id, User user);
}
