package com.dnd.Exercise.domain.field.service;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.*;

import com.dnd.Exercise.domain.field.dto.FieldMapper;
import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FieldServiceImpl implements FieldService{

    private final FieldRepository fieldRepository;
    private final UserFieldRepository userFieldRepository;
    private final FieldMapper fieldMapper;

    @Transactional
    @Override
    public void createField(CreateFieldReq createFieldReq, Long userId) {
        Field field = createFieldReq.toEntity(userId);
        fieldRepository.save(field);
    }

    @Override
    public FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond, Pageable pageable) {
        Page<Field> allFieldsWithFilter = fieldRepository.findAllFieldsWithFilter(
                findAllFieldsCond, pageable);

        List<Field> content = allFieldsWithFilter.getContent();
        Long totalCount = allFieldsWithFilter.getTotalElements();

        List<FindAllFieldsDto> fieldResList = content.stream().map(fieldMapper::toFindAllFieldsDto)
                .collect(Collectors.toList());

        return FindAllFieldsRes.builder().
                fieldsInfos(fieldResList).
                totalCount(totalCount).
                build();

    }

    @Override
    public FindFieldRes findField(Long id, User user) {
        Field myField = fieldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
        Boolean isMember = userFieldRepository.existsByFieldAndUser(myField, user);

        FieldDto fieldDto = fieldMapper.toFieldDto(myField);
        FindFieldRes.FindFieldResBuilder resBuilder = FindFieldRes.builder().fieldDto(fieldDto);

        if (isMember && myField.getFieldStatus() == IN_PROGRESS){
            Field opponentField = fieldRepository.findById(myField.getOpponent().getId())
                    .orElseThrow(() -> new BusinessException(NOT_FOUND));
            FindAllFieldsDto assignedFieldDto = fieldMapper.toFindAllFieldsDto(opponentField);
            resBuilder.assignedFieldDto(assignedFieldDto);
        }
        return resBuilder.build();
    }
}
