package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FieldServiceImpl implements FieldService{

    private final FieldRepository fieldRepository;

    @Transactional
    @Override
    public void createField(CreateFieldReq createFieldReq, Long userId) {
        Field field = createFieldReq.toEntity(userId);
        fieldRepository.save(field);
    }
}
