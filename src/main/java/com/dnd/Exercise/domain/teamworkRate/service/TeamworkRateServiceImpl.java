package com.dnd.Exercise.domain.teamworkRate.service;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.WinStatus;
import com.dnd.Exercise.domain.teamworkRate.dto.TeamworkRateMapper;
import com.dnd.Exercise.domain.teamworkRate.dto.request.PostTeamworkRateReq;
import com.dnd.Exercise.domain.teamworkRate.dto.response.GetTeamworkRateHistoryRes;
import com.dnd.Exercise.domain.teamworkRate.dto.response.TeamworkRateHistoryDto;
import com.dnd.Exercise.domain.teamworkRate.entity.TeamworkRate;
import com.dnd.Exercise.domain.teamworkRate.repository.TeamworkRateRepository;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.user.repository.UserRepository;
import com.dnd.Exercise.domain.userField.entity.UserField;
import com.dnd.Exercise.domain.userField.repository.UserFieldRepository;
import com.dnd.Exercise.global.error.exception.BusinessException;
import com.dnd.Exercise.global.util.field.FieldUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.dnd.Exercise.domain.field.entity.enums.FieldStatus.COMPLETED;
import static com.dnd.Exercise.global.error.dto.ErrorCode.NOT_COMPLETED;
import static com.dnd.Exercise.global.error.dto.ErrorCode.TEAMWORK_RATE_POSTED_ALREADY;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamworkRateServiceImpl implements TeamworkRateService {

    private final TeamworkRateRepository teamworkRateRepository;
    private final UserFieldRepository userFieldRepository;
    private final UserRepository userRepository;
    private final FieldUtil fieldUtil;
    private final TeamworkRateMapper teamworkRateMapper;

    @Override
    @Transactional
    public void postTeamworkRate(PostTeamworkRateReq postTeamworkRateReq, User user) {
        long fieldId = postTeamworkRateReq.getFieldId();
        int teamworkRate = postTeamworkRateReq.getTeamworkRate();

        Field field = fieldUtil.getField(fieldId);
        FieldType fieldType = field.getFieldType();

        fieldUtil.validateIsMember(user, field);
        validateIsFieldCompleted(field);
        validateIsPostingUndone(field, user);

        teamworkRateRepository.save(TeamworkRate.builder()
                .user(user)
                .field(field)
                .rate(teamworkRate)
                .build());

        if (fieldType == FieldType.TEAM_BATTLE || fieldType == FieldType.TEAM) {
            updateRatingOfTeamMembers(field);
        } else if (fieldType == FieldType.DUEL) {
            updateRatingOfOpponent(field);
        }
    }

    @Override
    @Transactional
    public int getTeamworkRateOfField(Field field) {
        int teamworkRateOfField = getRateGainOfField(field);
        return teamworkRateOfField;
    }

    @Override
    public GetTeamworkRateHistoryRes getTeamworkRateHistory(FieldType fieldType, Integer page, Integer size, User user) {
        Pageable pageable = PageRequest.of(page,size);
        Page<UserField> pagedUserFields = userFieldRepository.findCompletedFieldByUserAndType(user,fieldType,pageable);

        List<Field> completedFields = pagedUserFields.getContent()
                .stream().map(UserField::getField).collect(Collectors.toList());

        List<TeamworkRateHistoryDto> teamworkRateHistoryDtos = completedFields.stream()
                .map(field -> {
                    Integer teamworkRate = getRateGainOfField(field);
                    WinStatus winStatus = fieldUtil.getFieldWinStatus(field);
                    return teamworkRateMapper.from(field,teamworkRate,winStatus);})
                .collect(Collectors.toList());

        return new GetTeamworkRateHistoryRes().builder()
                .teamworkRateHistoryDtos(teamworkRateHistoryDtos)
                .currentPage(pagedUserFields.getNumber())
                .currentPageSize(pagedUserFields.getSize())
                .isFirstPage(pagedUserFields.isFirst())
                .isLastPage(pagedUserFields.isLast())
                .build();
    }

    private void validateIsFieldCompleted(Field field) {
        if (!COMPLETED.equals(field.getFieldStatus())) {
            throw new BusinessException(NOT_COMPLETED);
        }
    }

    private void validateIsPostingUndone(Field field, User user) {
        if (teamworkRateRepository.existsByFieldAndSubmitUser(field, user)) {
            throw new BusinessException(TEAMWORK_RATE_POSTED_ALREADY);
        }
    }

    private void updateRatingOfTeamMembers(Field field) {
        List<User> teamMembers = fieldUtil.getMembers(field.getId());
        teamMembers.forEach(user -> updateTeamworkRateOfUser(user));
    }

    private void updateRatingOfOpponent(Field field) {
        Field opponentField = field.getOpponent();
        User opponentUser = userRepository.findById(opponentField.getLeaderId()).get();
        updateTeamworkRateOfUser(opponentUser);
    }

    private void updateTeamworkRateOfUser(User user) {
        List<Field> completedFields = getCompletedFieldsOfUser(user);

        List<Integer> rateGainOfFields = completedFields.stream()
                .map(field -> getRateGainOfField(field))
                .collect(Collectors.toList());

        int totalRateGain = rateGainOfFields.stream().mapToInt(Integer::intValue).sum();
        int totalRatedField = Long.valueOf(rateGainOfFields.stream().filter(rateGain -> rateGain > 0).count()).intValue();

        int newTeamworkRate = totalRateGain / totalRatedField;
        user.updateTeamworkRate(newTeamworkRate);
    }

    private List<Field> getCompletedFieldsOfUser(User user) {
        List<UserField> userFields = userFieldRepository.findAllByUser(user);
        List<Field> completedFields = userFields.stream()
                .map(UserField::getField)
                .filter(field -> field.getFieldStatus() == COMPLETED)
                .collect(Collectors.toList());
        return completedFields;
    }

    private int getRateGainOfField(Field field) {
        FieldType fieldType = field.getFieldType();
        Long fieldId = field.getId();
        int rateGain = 0;

        if (fieldType == FieldType.TEAM_BATTLE || fieldType == FieldType.TEAM) {
            if (teamworkRateRepository.existsByField(field)) {
                Double avgRate = teamworkRateRepository.getAvgRateOfField(fieldId);
                rateGain = Long.valueOf(Math.round(avgRate)).intValue();
            }
        } else if (fieldType == FieldType.DUEL) {
            Field opponentField = field.getOpponent();
            if (teamworkRateRepository.existsByField(opponentField)) {
                rateGain = teamworkRateRepository.getRateOfField(opponentField.getId());
            }
        }
        return rateGain;
    }
}
