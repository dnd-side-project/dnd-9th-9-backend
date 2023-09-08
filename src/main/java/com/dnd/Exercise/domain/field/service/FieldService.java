package com.dnd.Exercise.domain.field.service;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.FieldSideDateReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldRecordsRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import java.time.LocalDate;
import org.springframework.data.domain.Pageable;

public interface FieldService {

    Long createField(CreateFieldReq createFieldReq, User user);

    FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond, Pageable pageable);

    FindFieldRes findField(Long id, User user);

    void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq);

    void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq);

    void deleteFieldId(Long id, User user);

    AutoMatchingRes autoMatching(FieldType fieldType, User user);

    GetFieldExerciseSummaryRes getFieldExerciseSummary(User user, Long fieldId, FieldSideDateReq summaryReq);

    GetRankingRes getTeamRanking(User user, Long fieldId, FieldSideDateReq teamRankingReq);

    GetRankingRes getDuelRanking(User user, Long fieldId, LocalDate date);

    FindAllFieldRecordsRes findAllFieldRecords(User user, Long fieldId, FindAllFieldRecordsReq recordsReq);

    FindFieldRecordDto findFieldRecord(User user, Long fieldId, Long exerciseId);

    void checkFieldStatus();

    FindFieldResultRes findFieldResult(User user, Long fieldId);

    void changeLeader(User user, Long fieldId, Long id);
}
