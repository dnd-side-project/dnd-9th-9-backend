package com.dnd.Exercise.domain.field.service;

import static com.dnd.Exercise.domain.field.dto.response.FieldRole.GUEST;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.LEADER;
import static com.dnd.Exercise.domain.field.dto.response.FieldRole.MEMBER;
import static com.dnd.Exercise.domain.field.entity.enums.FieldSide.AWAY;
import static com.dnd.Exercise.domain.field.entity.enums.FieldSide.HOME;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.IN_PROGRESS;
import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.RECRUITING;
import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.BURNED_CALORIE;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.EXERCISE_TIME;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.GOAL_ACHIEVED;
import static com.dnd.Exercise.domain.field.entity.enums.RankCriterion.RECORD_COUNT;
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
import com.dnd.Exercise.domain.field.dto.response.ElementWiseWinDto;
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
import com.dnd.Exercise.domain.field.entity.enums.RankCriterion;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.fieldEntry.repository.FieldEntryRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.s3.AwsS3Service;
import com.dnd.Exercise.global.util.field.FieldUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    private final FieldEntryRepository fieldEntryRepository;
    private final AwsS3Service awsS3Service;
    private final FieldUtil fieldUtil;
    private final UserRepository userRepository;
    private final String S3_FOLDER = "field-profile";


    private Field validateFieldAccess(User user, Long fieldId) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsMember(user, field);
        return field;
    }

    private void validateIsRecruiting(Field field) {
        if (field.getFieldStatus() == RECRUITING) {
            throw new BusinessException(RECRUITING_YET);
        }
    }

    private Boolean isFull(Field field) {
        return field.getCurrentSize() == field.getMaxSize();
    }


    private String s3Upload(MultipartFile profileImg) {
        String imgUrl = null;
        if (profileImg != null) {
            imgUrl = awsS3Service.upload(profileImg, S3_FOLDER);
        }
        return imgUrl;
    }

    private GetRankingRes toGetRankingRes(LocalDate date, List<Long> memberIds) {
        return GetRankingRes.builder()
                .recordCountRanking(getRankingByCriteria(RECORD_COUNT, date, memberIds))
                .exerciseTimeRanking(getRankingByCriteria(EXERCISE_TIME, date, memberIds))
                .burnedCalorieRanking(getRankingByCriteria(BURNED_CALORIE, date, memberIds))
                .goalAchievedCountRanking(getRankingByCriteria(GOAL_ACHIEVED, date, memberIds))
                .build();
    }

    private List<RankingDto> getRankingByCriteria(
            RankCriterion criterion, LocalDate date, List<Long> memberIds) {
        if (criterion == BURNED_CALORIE || criterion == GOAL_ACHIEVED) {
            return activityRingRepository.findTopByDynamicCriteria(
                    criterion, date, memberIds);
        }
        return exerciseRepository.findTopByDynamicCriteria(
                criterion, date, memberIds);
    }



    private WinStatus compareSummaries(List<Integer> mySummary, List<Integer> opponentSummary) {
        int result = IntStream.range(0, mySummary.size())
                .map(i -> Integer.compare(mySummary.get(i), opponentSummary.get(i)))
                .sum();

        if (result > 0) return WinStatus.WIN;
        if (result < 0) return WinStatus.LOSE;
        return WinStatus.DRAW;
    }

    private void addTotalScore(WinStatus winStatus, FindFieldResultDto home, FindFieldResultDto away) {
        if (winStatus == WinStatus.WIN) {
            home.addTotalScore(25);
        } else if (winStatus == WinStatus.LOSE) {
            away.addTotalScore(25);
        } else {
            home.addTotalScore(12.5);
            away.addTotalScore(12.5);
        }
    }

    private List<WinStatus> elementWiseWinToList(List<Integer> myScores, List<Integer> opponentScores){
        WinStatus recordCount = compareScore(myScores.get(0), opponentScores.get(0));
        WinStatus goalAchievedCount = compareScore(myScores.get(1), opponentScores.get(1));
        WinStatus burnedCalorie = compareScore(myScores.get(2), opponentScores.get(2));
        WinStatus exerciseTimeMinute = compareScore(myScores.get(3), opponentScores.get(3));
        return List.of(recordCount, goalAchievedCount, burnedCalorie, exerciseTimeMinute);
    }

    private WinStatus compareScore(Double myScore, Double opponentScore) {
        if (myScore > opponentScore) {
            return WinStatus.WIN;
        } else if (myScore < opponentScore) {
            return WinStatus.LOSE;
        } else {
            return WinStatus.DRAW;
        }
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
        if (user.getId().equals(myField.getLeaderId())){
            return LEADER;
        } else if (isMember) {
            return MEMBER;
        }else {
            return GUEST;
        }
    }


    @Transactional
    @Override
    public void createField(CreateFieldReq createFieldReq, User user) {
        fieldUtil.validateNotHavingField(user, createFieldReq.getFieldType());

        validateDuelMaxSize(createFieldReq.getFieldType(), createFieldReq.getMaxSize());

        Long userId = user.getId();
        String profileImg = s3Upload(createFieldReq.getProfileImg());

        Field field = createFieldReq.toEntity(userId, profileImg);
        Field savedField = fieldRepository.save(field);

        UserField userField = new UserField(user, savedField);
        userFieldRepository.save(userField);

        fieldEntryRepository.deleteAllByEntrantUserAndFieldType(user, field.getFieldType());
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
        Field myField = fieldUtil.getField(id);
        Boolean isMember = userFieldRepository.existsByFieldAndUser(myField, user);

        FieldDto fieldDto = fieldMapper.toFieldDto(myField);
        fieldDto.setFieldRole(determineFieldRole(user, myField, isMember));

        FindFieldRes.FindFieldResBuilder resBuilder = FindFieldRes.builder().fieldDto(fieldDto);

        if (isMember && myField.getFieldStatus() == IN_PROGRESS){
            Field opponentField = fieldUtil.getField(myField.getOpponent().getId());
            FindAllFieldsDto assignedFieldDto = fieldMapper.toFindAllFieldsDto(opponentField);
            resBuilder.assignedFieldDto(assignedFieldDto);
        }
        return resBuilder.build();
    }


    @Transactional
    @Override
    public void updateFieldProfile(Long id, User user, UpdateFieldProfileReq updateFieldProfileReq) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());
        fieldUtil.validateHaveOpponent(field);

        if(field.getProfileImg() != null){
            awsS3Service.deleteImage(field.getProfileImg());
        }
        String imgUrl = s3Upload(updateFieldProfileReq.getProfileImg());
        fieldMapper.updateFromProfileDto(updateFieldProfileReq, field);
        field.changeProfileImg(imgUrl);
    }


    @Transactional
    @Override
    public void updateFieldInfo(Long id, User user, UpdateFieldInfoReq updateFieldInfoReq) {
        Field field = fieldUtil.getField(id);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());
        fieldUtil.validateHaveOpponent(field);

        validateDuelMaxSize(updateFieldInfoReq.getFieldType(), updateFieldInfoReq.getMaxSize());

        fieldMapper.updateFromInfoDto(updateFieldInfoReq, field);
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
        UserField userField = fieldUtil.validateHavingField(user, fieldType);

        Field myField = userField.getField();

        fieldUtil.validateHaveOpponent(myField);
        fieldUtil.validateIsFull(myField);
        fieldUtil.validateIsLeader(user.getId(), myField.getLeaderId());

        List<Field> allFields = fieldRepository.findAllByCond(RECRUITING, fieldType, myField.getPeriod());

        // 기존에 추천해줬던 매치 제외시키는 로직 추가 필요
        Field resultField = allFields.stream()
                .filter(field -> !field.getId().equals(myField.getId()) && isFull(field))
                .min(Comparator.comparingInt(field ->
                                Math.abs(myField.getSkillLevel().ordinal() - field.getSkillLevel().ordinal())
                                + Math.abs(myField.getStrength().ordinal() - field.getStrength().ordinal())
                                + Math.abs(myField.getMaxSize() - field.getMaxSize())
                ))
                .orElseThrow(() -> new BusinessException(NO_SIMILAR_FIELD_FOUND));

        return fieldMapper.toAutoMatchingRes(resultField);
    }

    /**
     * HOME & opponent==null - 나의 필드 요약 정보 제공
     * HOME & opponent!=null - 나의 필드 요약 정보, 승리 여부, 상대방 필드 이름 제공
     * AWAY & opponent==null - 예외
     * AWAY & opponent!=null - 상대 필드 요약 정보 제공
     */
    @Override
    public GetFieldExerciseSummaryRes getFieldExerciseSummary(User user, Long fieldId, FieldSideDateReq summaryReq) {
        Field field = validateFieldAccess(user, fieldId);
        validateIsRecruiting(field);
        Field opponentField = field.getOpponent();

        FieldSide fieldSide = summaryReq.getFieldSide();
        LocalDate targetDate = summaryReq.getDate();

        if (AWAY.equals(fieldSide) && opponentField == null) {
            throw new BusinessException(OPPONENT_NOT_FOUND);
        }

        List<Integer> mySummary = fieldUtil.getFieldSummary(fieldId, targetDate);
        GetFieldExerciseSummaryRes summaryRes = new GetFieldExerciseSummaryRes(mySummary);

        if (HOME.equals(fieldSide) && opponentField != null) {
            List<Integer> opponentSummary = fieldUtil.getFieldSummary(opponentField.getId(), targetDate);

            WinStatus winStatus = compareSummaries(mySummary, opponentSummary);
            summaryRes.setWinStatus(winStatus);
            summaryRes.setOpponentFieldName(opponentField.getName());
        }

        return summaryRes;
    }

    @Override
    public GetRankingRes getTeamRanking(User user, Long fieldId, FieldSideDateReq teamRankingReq) {
        Field field = validateFieldAccess(user, fieldId);
        validateIsRecruiting(field);

        LocalDate date = teamRankingReq.getDate();
        FieldSide fieldSide = teamRankingReq.getFieldSide();

        if (AWAY.equals(fieldSide)){
            field = field.getOpponent();
        }
        Long targetId = field.getId();
        List<Long> memberIds = fieldUtil.getMemberIds(targetId);

        return toGetRankingRes(date, memberIds);
    }

    @Override
    public GetRankingRes getDuelRanking(User user, Long fieldId, LocalDate date) {
        Field field = validateFieldAccess(user, fieldId);
        validateIsRecruiting(field);

        Long opponentFieldId = field.getOpponent().getId();

        Long memberId = fieldUtil.getMemberIds(fieldId).get(0);
        Long opponentMemberId = fieldUtil.getMemberIds(opponentFieldId).get(0);
        List<Long> memberIds = List.of(memberId, opponentMemberId);

        return toGetRankingRes(date, memberIds);
    }

    @Override
    public FindAllFieldRecordsRes findAllFieldRecords(User user, Long fieldId,
            FindAllFieldRecordsReq recordsReq) {
        Field field = validateFieldAccess(user, fieldId);
        validateIsRecruiting(field);

        Long leaderId = field.getLeaderId();
        LocalDate targetDate = recordsReq.getDate();

        Pageable pageable = PageRequest.of(recordsReq.getPage(), recordsReq.getSize());

        List<Long> memberIds = fieldUtil.getMemberIds(fieldId);
        if (recordsReq.getFieldType() == DUEL){
            memberIds.addAll(fieldUtil.getMemberIds(field.getOpponent().getId()));
        }

        List<FindFieldRecordDto> recordList = exerciseRepository.findAllWithUser(
                targetDate, memberIds, pageable, leaderId);

        long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), field.getEndDate());

        FindAllFieldRecordsRes.FindAllFieldRecordsResBuilder resBuilder =
                FindAllFieldRecordsRes.builder()
                .recordList(recordList)
                .rule(field.getRule())
                .daysLeft(daysLeft);

        if (recordsReq.getFieldType() != TEAM){
            List<Integer> mySummary = fieldUtil.getFieldSummary(fieldId, targetDate);
            List<Integer> opponentSummary = fieldUtil.getFieldSummary(field.getOpponent().getId(), targetDate);

            WinStatus winStatus = compareSummaries(mySummary, opponentSummary);
            resBuilder.winStatus(winStatus);
        }

        return resBuilder.build();
    }

    @Override
    public FindFieldRecordDto findFieldRecord(User user, Long fieldId, Long exerciseId) {
        Field field = validateFieldAccess(user, fieldId);
        validateIsRecruiting(field);

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
            if (RECRUITING.equals(field.getFieldStatus()) && field.getOpponent() != null) {
                field.changeFieldStatus(IN_PROGRESS);
                field.updateDate(field.getPeriod());
            } else if (IN_PROGRESS.equals(field.getFieldStatus()) && LocalDate.now()
                    .equals(field.getEndDate())) {
                field.changeFieldStatus(COMPLETED);
            }
        }
    }

    //Badge 구현 후 BadgeList 불러오는 로직 추가 필요
    @Override
    public FindFieldResultRes findFieldResult(User user, Long fieldId) {
        Field myField = validateFieldAccess(user, fieldId);

        if(!COMPLETED.equals(myField.getFieldStatus())){
            throw new BusinessException(NOT_COMPLETED);
        }

        LocalDate startDate = myField.getStartDate();
        LocalDate endDate = myField.getEndDate();

        List<Integer> score = fieldUtil.getFieldSummary(myField.getId(), startDate, endDate);
        FindFieldResultDto home = new FindFieldResultDto(myField, score);
        FindFieldResultRes.FindFieldResultResBuilder resBuilder= FindFieldResultRes.builder()
                .period(myField.getPeriod())
                .home(home);

        if (!TEAM.equals(myField.getFieldType())){
            Field opponentField = myField.getOpponent();
            List<Integer> opponentScore = fieldUtil.getFieldSummary(opponentField.getId(), startDate, endDate);

            FindFieldResultDto away = new FindFieldResultDto(opponentField, opponentScore);

            List<WinStatus> elementWiseWinList = elementWiseWinToList(score, opponentScore);
            ElementWiseWinDto elementWiseWin = new ElementWiseWinDto(elementWiseWinList);
            elementWiseWinList.forEach(winStatus -> addTotalScore(winStatus, home, away));

            WinStatus winStatus = compareScore(home.getTotalScore(), away.getTotalScore());

            resBuilder.away(away).elementWiseWin(elementWiseWin).winStatus(winStatus);
        }

        return resBuilder.build();
    }

    @Transactional
    @Override
    public void changeLeader(User user, Long fieldId, Long id) {
        Field field = fieldUtil.getField(fieldId);
        fieldUtil.validateIsLeader(user.getId(), field.getLeaderId());

        User newLeader = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(NOT_FOUND));

        fieldUtil.validateIsMember(newLeader, field);

        field.changeLeader(newLeader.getId());
    }
}
