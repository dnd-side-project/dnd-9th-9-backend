package com.dnd.Exercise.domain.user.service;

import com.dnd.Exercise.domain.MemberEntry.repository.MemberEntryRepository;
import com.dnd.Exercise.domain.auth.service.AuthService;
import com.dnd.Exercise.domain.exercise.repository.ExerciseRepository;
import com.dnd.Exercise.domain.field.business.FieldBusiness;
import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldStatus;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.field.repository.FieldRepository;
import com.dnd.Exercise.domain.BattleEntry.repository.BattleEntryRepository;
import com.dnd.Exercise.domain.user.dto.UserMapper;
import com.dnd.Exercise.domain.user.dto.request.*;
import com.dnd.Exercise.domain.user.dto.response.GetFinalSummaryRes;
import com.dnd.Exercise.domain.user.dto.response.GetMatchSummaryRes;
import com.dnd.Exercise.domain.user.dto.response.GetProfileDetail;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.s3.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.dnd.Exercise.domain.field.entity.enums.FieldType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserFieldRepository userFieldRepository;
    private final BattleEntryRepository battleEntryRepository;
    private final MemberEntryRepository memberEntryRepository;
    private final FieldRepository fieldRepository;
    private final ExerciseRepository exerciseRepository;

    private final FieldBusiness fieldBusiness;

    private final UserMapper userMapper;

    private final AwsS3Service awsS3Service;
    private final AuthService authService;

    private final String S3_FOLDER = "user-profile";

    @Override
    public GetProfileDetail getProfileDetail(long userId) {
        User user = getUser(userId);
        return userMapper.from(user);
    }

    @Override
    @Transactional
    public void updateOnboardProfile(UpdateOnboardProfileReq updateOnboardProfileReq, long userId) {
        validateCalorieGoal(updateOnboardProfileReq);
        User user = getUser(userId);
        userMapper.updateFromDto(updateOnboardProfileReq,user);
    }

    @Override
    @Transactional
    public void postSkillLevel(PostSkillLevelReq postSkillLevelReq, long userId) {
        User user = getUser(userId);
        user.updateSkillLevel(postSkillLevelReq.getSkillLevel());
    }

    @Override
    @Transactional
    public void updateProfile(UpdateMyProfileReq updateMyProfileReq, long userId) {
        User user = getUser(userId);

        if (updateMyProfileReq.getDeletePrevImg() == true) {
            deleteMemoImgAtS3(user.getProfileImg());
            user.updateProfileImgUrl(null);
        }

        if (updateMyProfileReq.getNewProfileImg() != null) {
            String newImgUrl = postMemoImgAtS3(updateMyProfileReq.getNewProfileImg());
            user.updateProfileImgUrl(newImgUrl);
        }

        userMapper.updateFromDto(updateMyProfileReq,user);
    }

    @Override
    @Transactional
    public void updateAppleLinked(UpdateAppleLinkedReq updateAppleLinkedReq, long userId) {
        User user = getUser(userId);
        user.updateIsAppleLinked(updateAppleLinkedReq.getIsAppleLinked());
    }

    @Override
    @Transactional
    public void updateNotificationAgreed(UpdateNotificationAgreementReq updateNotificationAgreementReq, long userId) {
        User user = getUser(userId);
        user.updateIsNotificationAgreed(updateNotificationAgreementReq.getIsNotificationAgreed());
    }

    @Override
    @Transactional
    public void withdraw(long userId) {
        User user = getUser(userId);

        validateIsMatchingInProgress(user);

        deleteMyEntrantRequest(user);
        exitBeforeProgressFields(user);

        setToWithdrawUser(user);
        authService.logout(user.getId());
    }

    @Override
    public GetFinalSummaryRes getFinalSummary(long userId) {
        User user = getUser(userId);

        int activeDays = Long.valueOf(ChronoUnit.DAYS.between(user.getCreatedAt(), LocalDateTime.now())).intValue();
        int recordCounts = exerciseRepository.countByUserId(userId);
        int matchCounts = userFieldRepository.countAllCompletedFieldsByUserId(userId);
        int teamworkRate = user.getTeamworkRate();

        return GetFinalSummaryRes.builder()
                .activeDays(activeDays)
                .recordCounts(recordCounts)
                .matchCounts(matchCounts)
                .teamworkRate(teamworkRate)
                .build();
    }

    @Override
    public GetMatchSummaryRes getMatchSummary(long userId) {
        User user = getUser(userId);

        int teamMatchCount = userFieldRepository.countCompletedFieldsByUserIdAndFieldType(userId,List.of(TEAM_BATTLE));
        int duelMatchCount = userFieldRepository.countCompletedFieldsByUserIdAndFieldType(userId,List.of(DUEL));
        int teamCount = userFieldRepository.countCompletedFieldsByUserIdAndFieldType(userId,List.of(TEAM));
        int winningRate = getWinningRate(user);

        return GetMatchSummaryRes.builder()
                .teamMatchCount(teamMatchCount)
                .duelMatchCount(duelMatchCount)
                .teamCount(teamCount)
                .winningRate(winningRate)
                .build();
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).get();
    }

    private void validateCalorieGoal(UpdateOnboardProfileReq updateOnboardProfileReq) {
        if (updateOnboardProfileReq.getIsAppleLinked() == true) {
            if (updateOnboardProfileReq.getCalorieGoal() == null) {
                throw new BusinessException(ErrorCode.NEED_CALORIE_GOAL);
            }
        }
        else {
            if (updateOnboardProfileReq.getCalorieGoal() != null) {
                throw new BusinessException(ErrorCode.CALORIE_GOAL_UPDATE_UNAVAILABLE);
            }
        }
    }

    private String postMemoImgAtS3(MultipartFile memoImg) {
        String imgUrl = null;
        if (memoImg != null) {
            imgUrl = awsS3Service.upload(memoImg,S3_FOLDER);
        }
        return imgUrl;
    }

    private void deleteMemoImgAtS3(String fileName) {
        if(fileName != null) {
            awsS3Service.deleteImage(fileName);
        }
    }

    private void validateIsMatchingInProgress(User user) {
        List<UserField> myUserFields = userFieldRepository.findAllByUser(user);

        myUserFields.forEach(myUserField -> {
            Field myField = myUserField.getField();
            if (fieldBusiness.isFieldInProgress(myField)) {
                throw new BusinessException(ErrorCode.CANNOT_WITHDRAW_WHILE_IN_MATCH);
            }
        });
    }

    private void deleteMyEntrantRequest(User user) {
        memberEntryRepository.deleteAllByEntrantUser(user);
    }

    private void exitBeforeProgressFields(User user) {
        List<UserField> beforeProgressUserFields = userFieldRepository.findAllBeforeProgressFieldByUser(user);
        List<Field> beforeProgressFields = beforeProgressUserFields.stream().map(UserField::getField).collect(Collectors.toList());

        beforeProgressFields.forEach(field -> {
            FieldType fieldType = field.getFieldType();
            if (fieldType == DUEL) {
                deleteFieldAndEntries(field,user);
            }
            else {
                exitField(field,user);
            }
        });
    }

    private void deleteFieldAndEntries(Field field, User user) {
        long fieldId = field.getId();
        battleEntryRepository.deleteAllByEntrantField(field);
        battleEntryRepository.deleteAllByHostField(field);
        userFieldRepository.deleteByFieldAndUser(field,user);
        fieldRepository.deleteById(fieldId);
    }

    private void exitField(Field field, User user) {
        if (user.getId() == field.getLeaderId()) {
            exitOfLeader(field,user);
        }
        else {
            exitOfMember(field,user);
            battleEntryRepository.deleteAllByHostField(field);
            battleEntryRepository.deleteAllByEntrantField(field);
        }
    }

    private void exitOfLeader(Field field, User user) {
        long fieldId = field.getId();

        if (field.getCurrentSize() > 1) {
            throw new BusinessException(ErrorCode.SELECT_LEADER_BEFORE_WITHDRAW);
        }

        if (field.getFieldType() == TEAM_BATTLE) {
            battleEntryRepository.deleteAllByEntrantField(field);
            battleEntryRepository.deleteAllByHostField(field);
        }
        userFieldRepository.deleteByFieldAndUser(field,user);
        fieldRepository.deleteById(fieldId);
    }

    private void exitOfMember(Field field, User user) {
        userFieldRepository.deleteByFieldAndUser(field,user);
        field.subtractMember();
    }

    private void setToWithdrawUser(User user) {
        if (user.getProfileImg() != null) {
            awsS3Service.deleteImage(user.getProfileImg());
        }
        user.setToWithdrawUser();
    }

    private int getWinningRate(User user) {
        int totalMatchCount = userFieldRepository.countCompletedFieldsByUserIdAndFieldType(user.getId(),List.of(TEAM_BATTLE,DUEL));
        int winMatchCount = Optional.ofNullable(userFieldRepository.findByUserAndStatusInAndTypeIn(user, List.of(FieldStatus.COMPLETED), List.of(TEAM_BATTLE, DUEL)))
                .map(fields -> fields.stream()
                        .map(UserField::getField)
                        .filter(field -> fieldBusiness.getFieldWinStatus(field) == WinStatus.WIN)
                        .count())
                .orElse(0L).intValue();
        double winningRate = (double) winMatchCount / (double) totalMatchCount * 100.0;
        return (int) Math.round(winningRate);
    }
}
