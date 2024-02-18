package com.dnd.Exercise.domain.field.service;

import static com.dnd.Exercise.domain.field.dto.response.FieldRole.GUEST;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.LEADER;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.MEMBER;
import static com.dnd.Exercise.domain.field.entity.enums.FieldSide.AWAY;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.IN_PROGRESS;
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

import com.dnd.Exercise.domain.activityRing.repository.ActivityRingRepository;
import com.dnd.Exercise.domain.exercise.entity.Exercise;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.dto.FieldMapper;
import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.FieldSideDateReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.ElementWiseWin;
import com.dnd.Exercise.domain.field.dto.response.FieldDto;
import com.dnd.Exercise.domain.field.dto.response.FieldRole;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldRecordsRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes.FindFieldResBuilder;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.dto.response.RankingDto;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldSide;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
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
import com.dnd.Exercise.global.util.field.FieldUtil;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final ActivityRingRepository activityRingRepository;
    private final ExerciseRepository exerciseRepository;
    private final AwsS3Service awsS3Service;
    private final FieldUtil fieldUtil;
    private final UserRepository userRepository;
    private final RedisService redisService;
    private final TeamworkRateService teamworkRateService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;



    @Transactional
    @Override
    public Long createField(CreateFieldReq createFieldReq, User user) {
        fieldUtil.validateNotHavingField(user, createFieldReq.getFieldType());
        validateDuelMaxSize(createFieldReq.getFieldType(), createFieldReq.getMaxSize());

        String profileImg = s3Upload(createFieldReq.getProfileImg());

        Long userId = user.getId();
        Field field = createFieldReq.toEntity(userId, profileImg);
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
        Field myField = fieldUtil.getField(id);
        Boolean isMember = userFieldRepository.existsByFieldAndUser(myField, user);

        FieldDto myFieldDto = fieldMapper.toFieldDto(myField);
        myFieldDto.setFieldRole(determineFieldRole(user, myField, isMember));

        FindFieldResBuilder resBuilder = FindFieldRes.builder().fieldDto(myFieldDto);
        updateAssignedField(myField, isMember, resBuilder);
        return resBuilder.build();
    }

    @Transactional
    @Override
    public void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());

        deleteProfileImgIfPresent(field);

        String imgUrl = s3Upload(updateFieldProfileReq.getProfileImg());
        fieldMapper.updateFromProfileDto(updateFieldProfileReq, field);
        field.changeProfileImg(imgUrl);

        notificationService.sendFieldNotification(UPDATE_INFO, field);
    }


    @Transactional
    @Override
    public void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());
        fieldUtil.validateHaveOpponent(field);

        validateDuelMaxSize(updateFieldInfoReq.getFieldType(), updateFieldInfoReq.getMaxSize());

        fieldMapper.updateFromInfoDto(updateFieldInfoReq, field);

        notificationService.sendFieldNotification(UPDATE_INFO, field);
    }


    @Transactional
    @Override
    public void deleteFieldId(Long id, User user) {
        Field myField = fieldUtil.getField(id);

        fieldUtil.validateIsLeader(user.getId(), myField.getLeaderId());
        fieldUtil.validateHaveOpponent(myField);

        userFieldRepository.deleteAllByField(myField);
        fieldRepository.deleteById(id);
    }


    @Override
    public AutoMatchingRes autoMatching(FieldType fieldType, User user) {
        Field myField = checkAutoMatchingValidity(fieldType, user);

        Period period = myField.getPeriod();
        List<Field> allFittingFields = fieldRepository.findFullHouseFieldsByCond(RECRUITING, fieldType, period);

        String redisValue = redisService.getValues(REDIS_AUTO_PREFIX + myField.getId());
        List<String> matchedIds = getMatchedIds(redisValue);

        Field resultField = findBestMatchingField(myField, allFittingFields, matchedIds);
        setMatchedFieldsRedis(myField, redisValue, resultField);
        return fieldMapper.toAutoMatchingRes(resultField);
    }

    @Override
    public GetFieldExerciseSummaryRes getMyFieldExerciseSummary(User user, Long fieldId, LocalDate targetDate) {
        Field myField = validateFieldRecordAccess(user, fieldId);

        Field opponentField = myField.getOpponent();

        List<Integer> mySummary = fieldUtil.getFieldSummary(fieldId, targetDate);
        GetFieldExerciseSummaryRes summaryRes = GetFieldExerciseSummaryRes.of(mySummary);

        if (opponentField != null) {
            WinStatus winStatus = getWinStatus(targetDate, opponentField, mySummary);
            summaryRes.setWinStatus(winStatus);
            summaryRes.setOpponentFieldName(opponentField.getName());
        }
        return summaryRes;
    }

    @Override
    public GetFieldExerciseSummaryRes getOpponentFieldExerciseSummary(User user, Long fieldId, LocalDate targetDate) {
        Field myField = validateFieldRecordAccess(user, fieldId);

        Field opponentField = myField.getOpponent();
        validateOpponentPresence(opponentField);

        List<Integer> opponentSummary = fieldUtil.getFieldSummary(opponentField.getId(), targetDate);
        GetFieldExerciseSummaryRes summaryRes = GetFieldExerciseSummaryRes.of(opponentSummary);
        return summaryRes;
    }

    @Override
    public GetRankingRes getTeamRanking(User user, Long fieldId, FieldSideDateReq teamRankingReq) {
        Field field = validateFieldRecordAccess(user, fieldId);

        LocalDate date = teamRankingReq.getDate();
        FieldSide fieldSide = teamRankingReq.getFieldSide();

        if (AWAY.equals(fieldSide)) field = field.getOpponent();
        List<Long> memberIds = fieldUtil.getMemberIds(field.getId());
        return toGetRankingRes(date, memberIds);
    }

    @Override
    public GetRankingRes getDuelRanking(User user, Long fieldId, LocalDate date) {
        Field field = validateFieldRecordAccess(user, fieldId);

        Long opponentFieldId = field.getOpponent().getId();
        List<Long> targetUserIds = fieldUtil.getMemberIds(List.of(fieldId, opponentFieldId));
        return toGetRankingRes(date, targetUserIds);
    }

    @Override
    public FindAllFieldRecordsRes findAllFieldRecords(User user, Long fieldId, FindAllFieldRecordsReq recordsReq) {
        Field field = validateFieldRecordAccess(user, fieldId);
        LocalDate targetDate = recordsReq.getDate();
        Pageable pageable = getPageable(recordsReq);

        List<Long> targetUserIds = getTargetUserIds(field);

        Long leaderId = field.getLeaderId();
        Page<FindFieldRecordDto> fieldRecordPage = exerciseRepository.findAllByUserAndDate(
                targetDate, targetUserIds, pageable, leaderId);

        FindAllFieldRecordsRes result = FindAllFieldRecordsRes.from(fieldRecordPage, field, pageable);
        updateWinStatusIfNotTeam(field, targetDate, result);
        return result;
    }

    @Override
    public FindFieldRecordDto findFieldRecord(User user, Long fieldId, Long exerciseId) {
        Field field = validateFieldRecordAccess(user, fieldId);

        Exercise exercise = exerciseRepository.findWithUserById(exerciseId)
                .orElseThrow(() -> new BusinessException(EXERCISE_NOT_FOUND));
        fieldUtil.validateIsMember(exercise.getUser(), field);

        FindFieldRecordDto findFieldRecordDto = fieldMapper.toFindFieldRecordDto(exercise);
        findFieldRecordDto.setIsLeader(field.getLeaderId().equals(user.getId()));

        return findFieldRecordDto;
    }

    @Transactional
    @Override
    public void checkFieldStatus() {
        List<Field> fieldList = fieldRepository.findAll();
        for (Field field : fieldList) {
            updateFieldStatus(field);
        }
    }

    @Override
    public FindFieldResultRes findFieldResult(User user, Long fieldId) {
        Field myField = validateFieldRecordAccess(user, fieldId);

        validateCompleted(myField);

        LocalDate startDate = myField.getStartDate();
        LocalDate endDate = myField.getEndDate();

        List<Integer> mySummary = fieldUtil.getFieldSummary(myField.getId(), startDate, endDate);
        FindFieldResultDto home = FindFieldResultDto.from(myField, mySummary);

        int teamworkRate = teamworkRateService.getTeamworkRateOfField(myField);
        FindFieldResultRes result = FindFieldResultRes.from(home, myField, teamworkRate);

        updateIfNotTeam(myField, mySummary, home, result);

        return result;
    }


    @Transactional
    @Override
    public void changeLeader(User user, Long fieldId, Long id) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());

        User newLeader = userRepository.findById(id).orElseThrow(() -> new BusinessException(NOT_FOUND));
        fieldUtil.validateIsMember(newLeader, field);

        field.changeLeader(newLeader.getId());

        notificationService.sendFieldNotification(CHANGE_LEADER, field, newLeader.getName());
    }


    private void updateIfNotTeam(Field myField, List<Integer> mySummary, FindFieldResultDto home, FindFieldResultRes result) {
        if (!TEAM.equals(myField.getFieldType())){
            Field opponentField = myField.getOpponent();

            List<Integer> opponentSummary = getPeriodSummary(opponentField);
            FindFieldResultDto away = FindFieldResultDto.from(opponentField, opponentSummary);

            List<WinStatus> elementWiseWinStatus = getElementWiseWinStatus(mySummary, opponentSummary);
            elementWiseWinStatus.forEach(winStatus -> addTotalScore(winStatus, home, away));

            ElementWiseWin elementWiseWin = ElementWiseWin.from(elementWiseWinStatus);
            WinStatus winStatus = compareScore(home.getTotalScore(), away.getTotalScore());

            result.updateIfNotTeam(away, elementWiseWin, winStatus);
        }
    }

    private List<Integer> getPeriodSummary(Field field) {
        Long opponentFieldId = field.getId();
        LocalDate startDate = field.getStartDate();
        LocalDate endDate = field.getEndDate();

        List<Integer> summary = fieldUtil.getFieldSummary(opponentFieldId, startDate, endDate);
        return summary;
    }


    private Field validateFieldRecordAccess(User user, Long fieldId) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsMember(user, field);
        validateNotRecruiting(field);
        return field;
    }

    private void validateNotRecruiting(Field field) {
        if (RECRUITING.equals(field.getFieldStatus())) {
            throw new BusinessException(RECRUITING_YET);
        }
    }

    private void validateCompleted(Field myField) {
        if(!COMPLETED.equals(myField.getFieldStatus())){
            throw new BusinessException(NOT_COMPLETED);
        }
    }

    private void addTotalScore(WinStatus winStatus, FindFieldResultDto home, FindFieldResultDto away) {
        if (winStatus == WinStatus.WIN)
            home.addTotalScore(1);
        else if (winStatus == WinStatus.LOSE)
            away.addTotalScore(1);
    }

    private List<WinStatus> getElementWiseWinStatus(List<Integer> myScores, List<Integer> opponentScores){
        List<WinStatus> winStatusList = new ArrayList<>();
        for (int idx = 0; idx < myScores.size(); idx++){
            WinStatus winStatus = compareScore(myScores.get(idx), opponentScores.get(idx));
            winStatusList.add(winStatus);
        }
        return winStatusList;
    }

    private WinStatus compareScore(Double myScore, Double opponentScore) {
        if (myScore > opponentScore) return WinStatus.WIN;
        else if (myScore < opponentScore) return WinStatus.LOSE;
        else return WinStatus.DRAW;
    }

    private WinStatus compareScore(Integer myScore, Integer opponentScore) {
        return compareScore(myScore.doubleValue(), opponentScore.doubleValue());
    }


    private static void validateDuelMaxSize(FieldType fieldType, int maxSize) {
        if (DUEL.equals(fieldType) && maxSize != 1) {
            throw new BusinessException(DUEL_MAX_ONE);
        }
    }

    private FieldRole determineFieldRole(User user, Field myField, Boolean isMember) {
        Long userId = user.getId();
        Long leaderId = myField.getLeaderId();
        if (userId.equals(leaderId)) return LEADER;
        else if (isMember) return MEMBER;
        else return GUEST;
    }

    private void updateAssignedField(Field myField, Boolean isMember, FindFieldResBuilder resBuilder) {
        Field opponentField = myField.getOpponent();
        if (isMember && opponentField != null){
            FindAllFieldsDto assignedFieldDto = fieldMapper.toFindAllFieldsDto(opponentField);
            resBuilder.assignedFieldDto(assignedFieldDto);
        }
    }

    private void deleteProfileImgIfPresent(Field field) {
        if(field.getProfileImg() != null)
            awsS3Service.deleteImage(field.getProfileImg());
    }

    private void setMatchedFieldsRedis(Field myField, String redisValue, Field resultField) {
        redisService.setValues(REDIS_AUTO_PREFIX + myField.getId(),
                redisValue + REDIS_AUTO_SPLIT_REGEX + resultField.getId(),
                Duration.ofSeconds(60));
    }

    private Field findBestMatchingField(Field myField, List<Field> allFittingFields, List<String> matchedIds) {
        return allFittingFields.stream()
                .filter(field -> isNotMyField(myField, field) && isNotMatchedField(matchedIds, field))
                .min(Comparator.comparingInt(field -> calculateFieldDifference(myField, field)))
                .orElseThrow(() -> handleNoMatchingFieldFound(myField));
    }

    private BusinessException handleNoMatchingFieldFound(Field myField) {
        redisService.deleteValues(REDIS_AUTO_PREFIX + myField.getId());
        return new BusinessException(NO_SIMILAR_FIELD_FOUND);
    }

    private int calculateFieldDifference(Field myField, Field field) {
        return Math.abs(myField.getSkillLevel().ordinal() - field.getSkillLevel().ordinal())
                + Math.abs(myField.getStrength().ordinal() - field.getStrength().ordinal())
                + Math.abs(myField.getMaxSize() - field.getMaxSize());
    }

    private boolean isNotMatchedField(List<String> matchedIds, Field field) {
        return !matchedIds.contains(String.valueOf(field.getId()));
    }

    private boolean isNotMyField(Field myField, Field field) {
        return !field.getId().equals(myField.getId());
    }

    private List<String> getMatchedIds(String redisValue) {
        List<String> matchedIds = new ArrayList<>();
        if (redisValue != null){
            List<String> ids = List.of(redisValue.split(REDIS_AUTO_SPLIT_REGEX));
            matchedIds.addAll(ids);
        }
        return matchedIds;
    }

    private Field checkAutoMatchingValidity(FieldType fieldType, User user) {
        UserField userField = fieldUtil.validateHavingField(user, fieldType);
        Field myField = userField.getField();

        fieldUtil.validateHaveOpponent(myField);
        fieldUtil.validateIsFull(myField);
        fieldUtil.validateIsLeader(user.getId(), myField.getLeaderId());
        return myField;
    }

    private void validateOpponentPresence(Field opponentField) {
        if (opponentField == null) {
            throw new BusinessException(OPPONENT_NOT_FOUND);
        }
    }

    private void updateWinStatusIfNotTeam(Field field, LocalDate targetDate, FindAllFieldRecordsRes result) {
        if (field.getFieldType() != TEAM){
            List<Integer> mySummary = fieldUtil.getFieldSummary(field.getId(), targetDate);
            WinStatus winStatus = getWinStatus(targetDate, field.getOpponent(), mySummary);
            result.setWinStatus(winStatus);
        }
    }

    private WinStatus getWinStatus(LocalDate targetDate, Field opponentField, List<Integer> mySummary) {
        List<Integer> opponentSummary = fieldUtil.getFieldSummary(opponentField.getId(), targetDate);
        return fieldUtil.compareSummaries(mySummary, opponentSummary);
    }

    private List<Long> getTargetUserIds(Field field) {
        List<Long> memberIds;
        Long myFieldId = field.getId();
        if (field.getFieldType() == DUEL){
            Long opponentFieldId = field.getOpponent().getId();
            memberIds = fieldUtil.getMemberIds(List.of(opponentFieldId, myFieldId));
        }
        else {
            memberIds = fieldUtil.getMemberIds(myFieldId);
        }
        return memberIds;
    }

    private PageRequest getPageable(FindAllFieldRecordsReq recordsReq) {
        int page = recordsReq.getPage();
        int size = recordsReq.getSize();
        return PageRequest.of(page, size);
    }

    private void updateFieldStatus(Field field) {
        if (RECRUITING.equals(field.getFieldStatus()) && field.getOpponent() != null) {
            field.changeFieldStatus(IN_PROGRESS);
            field.updateDate(field.getPeriod());
        } else if (IN_PROGRESS.equals(field.getFieldStatus()) && LocalDate.now().equals(field.getEndDate())) {
            field.changeFieldStatus(COMPLETED);
        }
    }

    private String s3Upload(MultipartFile profileImg) {
        String imgUrl = null;
        if (profileImg != null)
            imgUrl = awsS3Service.upload(profileImg, S3_FILED_PROFILE_FOLDER_NAME);
        return imgUrl;
    }

    private GetRankingRes toGetRankingRes(LocalDate date, List<Long> memberIds) {
        List<RankingDto> recordCount = getRankingByCriteria(RECORD_COUNT, date, memberIds);
        List<RankingDto> exerciseTime = getRankingByCriteria(EXERCISE_TIME, date, memberIds);
        List<RankingDto> burnedCalorie = getRankingByCriteria(BURNED_CALORIE, date, memberIds);
        List<RankingDto> goalAchievedCount = getRankingByCriteria(GOAL_ACHIEVED, date, memberIds);
        return GetRankingRes.from(recordCount, exerciseTime, burnedCalorie, goalAchievedCount);
    }

    private List<RankingDto> getRankingByCriteria(RankCriterion criterion, LocalDate date, List<Long> memberIds) {
        if (criterion == BURNED_CALORIE || criterion == GOAL_ACHIEVED)
            return activityRingRepository.findTopByDynamicCriteria(criterion, date, memberIds);
        return exerciseRepository.findTopByDynamicCriteria(criterion, date, memberIds);
    }
}
