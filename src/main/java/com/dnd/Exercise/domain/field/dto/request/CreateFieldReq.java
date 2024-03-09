package com.dnd.Exercise.domain.field.dto.request;

import static com.dnd.Exercise.domain.field.entity.enums.FieldType.DUEL;
import static com.dnd.Exercise.global.error.dto.ErrorCode.DUEL_MAX_ONE;

import com.dnd.Exercise.domain.field.entity.Field;
import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import com.dnd.Exercise.global.error.exception.BusinessException;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFieldReq {

    @NotBlank
    @ApiModelProperty(notes = "이름", required = true)
    private String name;

    @ApiModelProperty(notes = "프로필 이미지, MultipartFile")
    private MultipartFile profileImg;

    @NotNull
    @ApiModelProperty(notes = "운동강도", required = true, example = "LOW | MODERATE | HIGH")
    private Strength strength;

    @NotNull
    @ApiModelProperty(notes = "카테고리", required = true, example = "GAIN | LOSS | MAINTENANCE | PROFILE")
    private Goal goal;

    @ApiModelProperty(notes = "팀 규칙")
    private String rule;

    @Range(min = 1, max = 10)
    @NotNull
    @ApiModelProperty(notes = "팀 인원수 - 최소 1, 최대 10", required = true)
    private int maxSize;

    @NotNull
    @ApiModelProperty(notes = "진행기간", required = true, example = "ONE_WEEK | TWO_WEEKS | THREE_WEEKS")
    private Period period;

    @ApiModelProperty(notes = "팀 소개")
    private String description;

    @NotNull
    @ApiModelProperty(notes = "매칭", required = true, example = "DUEL | TEAM_BATTLE | TEAM")
    private FieldType fieldType;

    @NotNull
    @ApiModelProperty(notes = "운동레벨", required = true,
            example = "BEGINNER | INTERMEDIATE | ADVANCED_INTERMEDIATE | EXPERT")
    private SkillLevel skillLevel;

    public Field toEntity(Long leaderId, String profileImg){
        return Field.builder()
                .name(name)
                .profileImg(profileImg)
                .strength(strength)
                .goal(goal)
                .rule(rule)
                .maxSize(maxSize)
                .period(period)
                .description(description)
                .leaderId(leaderId)
                .fieldType(fieldType)
                .skillLevel(skillLevel)
                .build();
    }

    public void validateDuelMaxSize() {
        if (DUEL.equals(fieldType) && maxSize != 1) {
            throw new BusinessException(DUEL_MAX_ONE);
        }
    }
}

