package com.dnd.Exercise.domain.field.service;

import static com.dnd.Exercise.domain.field.entity.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.global.error.dto.ErrorCode.*;

import com.dnd.Exercise.domain.field.dto.FieldMapper;
import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
        Field myField = getField(id);
        Boolean isMember = userFieldRepository.existsByFieldAndUser(myField, user);

        FieldDto fieldDto = fieldMapper.toFieldDto(myField);
        FindFieldRes.FindFieldResBuilder resBuilder = FindFieldRes.builder().fieldDto(fieldDto);

        if (isMember && myField.getFieldStatus() == IN_PROGRESS){
            Field opponentField = getField(myField.getOpponent().getId());
            FindAllFieldsDto assignedFieldDto = fieldMapper.toFindAllFieldsDto(opponentField);
            resBuilder.assignedFieldDto(assignedFieldDto);
        }
        return resBuilder.build();
    }

    @Transactional
    @Override
    public void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq) {
        Field field = getField(id);
        isLeader(user, field);
        isRecruiting(field);
        fieldMapper.updateFromDto(updateFieldProfileReq, field);
    }


    @Transactional
    @Override
    public void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq) {
        Field field = getField(id);
        isLeader(user, field);
        isRecruiting(field);
        fieldMapper.updateFromInfoDto(updateFieldInfoReq, field);
    }

    private void isLeader(User user, Field field) {
        if (!user.getId().equals(field.getLeaderId())){
            throw new BusinessException(FORBIDDEN);
        }
    }

    private void isRecruiting(Field field) {
        if (Arrays.asList(IN_PROGRESS, COMPLETED).contains(field.getFieldStatus())){
            throw new BusinessException(INVALID_STATUS);
        }
    }

    private Field getField(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }
}
