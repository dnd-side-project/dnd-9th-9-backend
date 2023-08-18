package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.Period;
import com.dnd.Exercise.domain.field.entity.Goal;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.entity.SkillLevel;
import com.dnd.Exercise.domain.field.entity.Strength;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class FindAllFieldsCond {
    @ApiModelProperty(notes = "매칭유형", required = true, example = "DUEL | TEAM_BATTLE | TEAM")
    private FieldType fieldType;
    @ApiModelProperty(notes = "팀 인원수 - 최소 1, 최대 10")
    private Integer memberCount;
    @ApiModelProperty(notes = "운동레벨", example = "[BEGINNER, INTERMEDIATE, ADVANCED_INTERMEDIATE, EXPERT]")
    private List<SkillLevel> skillLevel;
    @ApiModelProperty(notes = "운동강도", example = "[LOW, MODERATE, HIGH]")
    private List<Strength> strength;
    @ApiModelProperty(notes = "진행기간", example = "[ONE_WEEK, TWO_WEEKS, THREE_WEEKS]")
    private List<Period> period;
    @ApiModelProperty(notes = "카테고리", example = "[GAIN, LOSS, MAINTENANCE, PROFILE]")
    private List<Goal> goal;
}
