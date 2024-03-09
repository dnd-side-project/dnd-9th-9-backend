package com.dnd.Exercise.domain.field.service;

import static com.dnd.Exercise.domain.field.entity.enums.FieldSide.AWAY;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.RECRUITING;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.BURNED_CALORIE;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.EXERCISE_TIME;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.GOAL_ACHIEVED;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.RECORD_COUNT;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.CHANGE_LEADER;
import static com.dnd.Exercise.domain.notification.entity.NotificationTopic.UPDATE_INFO;
import static com.dnd.Exercise.global.common.Constants.REDIS_AUTO_PREFIX;
import static com.dnd.Exercise.global.common.Constants.REDIS_AUTO_SPLIT_REGEX;
import static com.dnd.Exercise.global.common.Constants.S3_FILED_PROFILE_FOLDER_NAME;
import static com.dnd.Exercise.global.error.dto.ErrorCode.*;

import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.dto.FieldMapper;
import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.FieldSideDateReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FieldRole;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldRecordsRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldSide;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.field.event.CreateEvent;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.notification.service.NotificationService;
import com.dnd.Exercise.domain.teamworkRate.service.TeamworkRateService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.common.RedisService;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.s3.AwsS3Service;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class FieldServiceImpl implements FieldService{
    private final FieldRepository fieldRepository;
    private final UserFieldRepository userFieldRepository;
    private final FieldMapper fieldMapper;
    private final ExerciseRepository exerciseRepository;
    private final AwsS3Service awsS3Service;
    private final FieldBusiness fieldBusiness;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final TeamworkRateService teamworkRateService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;



    @Transactional
    @Override
    public Long createField(CreateFieldReq createFieldReq, User user) {
        checkCreateFieldValidity(createFieldReq, user);

        String profileImg = s3Upload(createFieldReq.getProfileImg());
        Field field = createFieldReq.toEntity(user.getId(), profileImg);
        Field savedField = fieldRepository.save(field);

        eventPublisher.publishEvent(CreateEvent.newEvent(user, savedField));

        return savedField.getId();
    }


    @Override
    public FindAllFieldsRes findAllFields(FindAllFieldsCond findAllFieldsCond) {
        List<Field> fieldList = fieldRepository.findAllFieldsWithFilter(findAllFieldsCond);

        List<FindAllFieldsDto> fieldsInfos = fieldList.stream()
                .map(fieldMapper::toFindAllFieldsDto)
                .collect(Collectors.toList());

        return FindAllFieldsRes.from(fieldsInfos, findAllFieldsCond.getSize());
    }

    @Override
    public Long countAllFields(FindAllFieldsCond findAllFieldsCond) {
        return fieldRepository.countAllFieldsWithFilter(findAllFieldsCond);
    }

    @Override
    public FindFieldRes findField(Long id, User user) {
        Field myField = fieldBusiness.getField(id);
        Boolean isMember = userFieldRepository.existsByFieldAndUser(myField, user);

        FieldRole fieldRole = myField.determineFieldRole(user, isMember);
        FieldDto myFieldDto = FieldDto.from(myField, fieldRole);

        FindFieldRes result = FindFieldRes.from(myFieldDto);
        result.updateAssignedField(myField, isMember);
        return result;
    }

    @Transactional
    @Override
    public void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq) {
        Field field = fieldBusiness.getField(id);
        field.validateIsLeader(user.getId());

        fieldBusiness.deleteProfileImgIfPresent(field);

        String imgUrl = s3Upload(updateFieldProfileReq.getProfileImg());
        fieldMapper.updateFromProfileDto(updateFieldProfileReq, imgUrl, field);

        notificationService.sendFieldNotification(UPDATE_INFO, field);
    }


    @Transactional
    @Override
    public void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq) {
        Field field = fieldBusiness.getField(id);
        checkUpdateFieldInfo(user, updateFieldInfoReq, field);

        fieldMapper.updateFromInfoDto(updateFieldInfoReq, field);
        notificationService.sendFieldNotification(UPDATE_INFO, field);
    }


    @Transactional
    @Override
    public void deleteField(Long id, User user) {
        Field myField = fieldBusiness.getField(id);
        checkDeleteFieldValidity(user, myField);

        userFieldRepository.deleteAllByField(myField);
        fieldRepository.deleteById(id);
    }


    @Override
    public AutoMatchingRes autoMatching(FieldType fieldType, User user) {
        Field myField = checkAutoMatchingValidity(fieldType, user);

        Period period = myField.getPeriod();
        List<Field> allFittingFields = fieldRepository.findFullHouseFieldsByCond(RECRUITING, fieldType, period);
        Field resultField = getBestMatchingField(myField, allFittingFields);

        return fieldMapper.toAutoMatchingRes(resultField);
    }

    @Override
    public GetFieldExerciseSummaryRes getMyFieldExerciseSummary(User user, Long fieldId, LocalDate targetDate) {
        Field myField = validateFieldRecordAccess(user, fieldId);

        Field opponentField = myField.getOpponent();
        List<Integer> mySummary = fieldBusiness.getFieldSummary(fieldId, targetDate);
        GetFieldExerciseSummaryRes summaryRes = GetFieldExerciseSummaryRes.of(mySummary);

        if (opponentField != null) {
            WinStatus winStatus = getWinStatus(targetDate, opponentField, mySummary);
            summaryRes.changeFrom(winStatus, opponentField.getName());
        }
        return summaryRes;
    }

    @Override
    public GetFieldExerciseSummaryRes getOpponentFieldExerciseSummary(User user, Long fieldId, LocalDate targetDate) {
        Field myField = validateFieldRecordAccess(user, fieldId);
        myField.validateOpponentPresence();

        Field opponentField = myField.getOpponent();
        List<Integer> opponentSummary = fieldBusiness.getFieldSummary(opponentField.getId(), targetDate);
        return GetFieldExerciseSummaryRes.of(opponentSummary);
    }

    @Override
    public GetRankingRes getTeamRanking(User user, Long fieldId, FieldSideDateReq teamRankingReq) {
        Field field = validateFieldRecordAccess(user, fieldId);

        LocalDate date = teamRankingReq.getDate();
        FieldSide fieldSide = teamRankingReq.getFieldSide();

        if (AWAY.equals(fieldSide)) field = field.getOpponent();
        List<Long> memberIds = fieldBusiness.getMemberIds(field.getId());
        return toGetRankingRes(date, memberIds);
    }

    @Override
    public GetRankingRes getDuelRanking(User user, Long fieldId, LocalDate date) {
        Field field = validateFieldRecordAccess(user, fieldId);

        Long opponentFieldId = field.getOpponent().getId();
        List<Long> targetUserIds = fieldBusiness.getMemberIds(List.of(fieldId, opponentFieldId));
        return toGetRankingRes(date, targetUserIds);
    }

    @Override
    public FindAllFieldRecordsRes findAllFieldRecords(User user, Long fieldId, FindAllFieldRecordsReq recordsReq) {
        Field field = validateFieldRecordAccess(user, fieldId);
        LocalDate targetDate = recordsReq.getDate();
        Pageable pageable = getPageable(recordsReq);

        List<Long> targetUserIds = fieldBusiness.getTargetUserIds(field);
        Page<FindFieldRecordDto> fieldRecordPage = exerciseRepository.findAllByUserAndDate(
                targetDate, targetUserIds, pageable, field.getLeaderId());

        FindAllFieldRecordsRes result = FindAllFieldRecordsRes.from(fieldRecordPage, field, pageable);
        updateWinStatusIfNotTeam(field, targetDate, result);
        return result;
    }

    @Override
    public FindFieldRecordDto findFieldRecord(User user, Long fieldId, Long exerciseId) {
        Field field = validateFieldRecordAccess(user, fieldId);

        Exercise exercise = exerciseRepository.findWithUserById(exerciseId)
                .orElseThrow(() -> new BusinessException(EXERCISE_NOT_FOUND));
        fieldBusiness.validateIsMember(exercise.getUser(), field);

        FindFieldRecordDto findFieldRecordDto = fieldMapper.toFindFieldRecordDto(exercise);
        findFieldRecordDto.setIsLeader(field.getLeaderId().equals(user.getId()));

        return findFieldRecordDto;
    }

    @Transactional
    @Override
    public void checkFieldStatus() {
        List<Field> fieldList = fieldRepository.findAll();
        for (Field field : fieldList) {
            field.updateFieldStatusForScheduler();
        }
    }

    @Override
    public FindFieldResultRes findFieldResult(User user, Long fieldId) {
        Field myField = validateFieldRecordAccess(user, fieldId);
        myField.validateCompleted();

        LocalDate startDate = myField.getStartDate();
        LocalDate endDate = myField.getEndDate();

        List<Integer> mySummary = fieldBusiness.getFieldSummary(myField.getId(), startDate, endDate);
        FindFieldResultDto home = FindFieldResultDto.from(myField, mySummary);

        int teamworkRate = teamworkRateService.getTeamworkRateOfField(myField);
        FindFieldResultRes result = FindFieldResultRes.from(home, myField, teamworkRate);

        fieldBusiness.updateIfNotTeam(myField, mySummary, home, result);

        return result;
    }

    @Transactional
    @Override
    public void changeLeader(User user, Long fieldId, Long id) {
        Field field = fieldBusiness.getField(fieldId);
        field.validateIsLeader(user.getId());

        User newLeader = userRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND));
        fieldBusiness.validateIsMember(newLeader, field);

        field.changeLeader(newLeader.getId());
        notificationService.sendFieldNotification(CHANGE_LEADER, field, newLeader.getName());
    }


    private Field validateFieldRecordAccess(User user, Long fieldId) {
        Field field = fieldBusiness.getField(fieldId);
        fieldBusiness.validateIsMember(user, field);
        field.validateNotRecruiting();
        return field;
    }

    private void checkCreateFieldValidity(CreateFieldReq createFieldReq, User user) {
        fieldBusiness.validateNotHavingField(user, createFieldReq.getFieldType());
        createFieldReq.validateDuelMaxSize();
    }

    private void checkUpdateFieldInfo(User user, UpdateFieldInfoReq updateFieldInfoReq, Field field) {
        field.validateIsLeader(user.getId());
        field.validateHaveOpponent();
        updateFieldInfoReq.validateDuelMaxSize();
    }

    private Field checkAutoMatchingValidity(FieldType fieldType, User user) {
        UserField userField = fieldBusiness.validateHavingField(user, fieldType);
        Field myField = userField.getField();

        myField.validateHaveOpponent();
        myField.validateIsFull();
        myField.validateIsLeader(user.getId());
        return myField;
    }

    private void checkDeleteFieldValidity(User user, Field myField) {
        myField.validateIsLeader(user.getId());
        myField.validateHaveOpponent();
    }

    private void updateWinStatusIfNotTeam(Field field, LocalDate targetDate, FindAllFieldRecordsRes result) {
        if (field.getFieldType() != TEAM){
            List<Integer> mySummary = fieldBusiness.getFieldSummary(field.getId(), targetDate);
            List<Integer> opponentSummary = fieldBusiness.getFieldSummary(field.getOpponent().getId(), targetDate);
            WinStatus winStatus = fieldBusiness.compareSummaries(mySummary, opponentSummary);
            result.setWinStatus(winStatus);
        }
    }

    private WinStatus getWinStatus(LocalDate targetDate, Field opponentField, List<Integer> mySummary) {
        List<Integer> opponentSummary = fieldBusiness.getFieldSummary(opponentField.getId(), targetDate);
        return fieldBusiness.compareSummaries(mySummary, opponentSummary);
    }

    private Field getBestMatchingField(Field myField, List<Field> allFittingFields) {
        String redisValue = redisService.getValues(REDIS_AUTO_PREFIX + myField.getId());
        List<String> matchedIds = fieldBusiness.getMatchedIds(redisValue);
        Field resultField = fieldBusiness.findBestMatchingField(myField, allFittingFields, matchedIds);
        setMatchedFieldsRedis(myField, redisValue, resultField);
        return resultField;
    }

    private void setMatchedFieldsRedis(Field myField, String redisValue, Field resultField) {
        redisService.setValues(REDIS_AUTO_PREFIX + myField.getId(),
                redisValue + REDIS_AUTO_SPLIT_REGEX + resultField.getId(),
                Duration.ofSeconds(60));
    }

    private GetRankingRes toGetRankingRes(LocalDate date, List<Long> memberIds) {
        List<RankingDto> recordCount = fieldRepository.findTopByExerciseCriteria(RECORD_COUNT, date, memberIds);
        List<RankingDto> exerciseTime = fieldRepository.findTopByExerciseCriteria(EXERCISE_TIME, date, memberIds);
        List<RankingDto> burnedCalorie = fieldRepository.findTopByActivityCriteria(BURNED_CALORIE, date, memberIds);
        List<RankingDto> goalAchievedCount = fieldRepository.findTopByActivityCriteria(GOAL_ACHIEVED, date, memberIds);
        return GetRankingRes.from(recordCount, exerciseTime, burnedCalorie, goalAchievedCount);
    }

    private String s3Upload(MultipartFile profileImg) {
        if (profileImg == null) return null;
        return awsS3Service.upload(profileImg, S3_FILED_PROFILE_FOLDER_NAME);
    }

    private PageRequest getPageable(FindAllFieldRecordsReq recordsReq) {
        int page = recordsReq.getPage();
        int size = recordsReq.getSize();
        return PageRequest.of(page, size);
    }
}
