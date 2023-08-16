package com.dnd.Exercise.domain.userField.service;

import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_FOUND;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.userField.dto.UserFieldMapper;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserFieldServiceImpl implements UserFieldService {

    private final UserFieldRepository userFieldRepository;
    private final FieldRepository fieldRepository;
    private final UserFieldMapper userFieldMapper;

    @Override
    public List<FindAllMembersRes> findAllMembers(Long fieldId) {
        Field field = getField(fieldId);
        Long leaderId = field.getLeaderId();
        List<UserField> allMembers = userFieldRepository.findAllByField(fieldId);

        return allMembers.stream().map(member -> {
                    FindAllMembersRes findAllMembersRes =
                            userFieldMapper.toFindAllMembersRes(member, member.getUser());
                    findAllMembersRes.setIsLeader(leaderId.equals(findAllMembersRes.getId()));
                    return findAllMembersRes;
                })
                .collect(Collectors.toList());
    }

    private Field getField(Long id) {
        return fieldRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));
    }
}
