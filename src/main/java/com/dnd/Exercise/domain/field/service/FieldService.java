package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface FieldService {

    void createField(CreateFieldReq createFieldReq, Long userId);

    FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond, Pageable pageable);

    FindFieldRes findField(Long id, User user);

    void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq);

    void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq);

    void deleteFieldId(Long id, User user);

    AutoMatchingRes autoMatching(FieldType fieldType, User user);
}
