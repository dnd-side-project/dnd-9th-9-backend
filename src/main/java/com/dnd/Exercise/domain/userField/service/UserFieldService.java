package com.dnd.Exercise.domain.userField.service;

import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserFieldService {

    List<FindAllMembersRes> findAllMembers(Long fieldId);

    List<FindAllFieldsDto> findAllMyInProgressFields(User user);

    List<FindAllFieldsDto> findAllMyCompletedFields(User user, FieldType fieldType, Pageable pageable);

    FindMyBattleStatusRes findMyBattleStatus(User user, BattleType battleType);

    FindMyTeamStatusRes findMyTeamStatus(User user);

    void ejectMember(User user, Long fieldId, List<Long> ids);

    void exitField(User user, Long id);

    void cheerMember(User user, Long id);

    void alertMembers(User user, Long id);
}
