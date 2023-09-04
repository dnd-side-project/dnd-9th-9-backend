package com.dnd.Exercise.domain.field.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.Period;
import com.dnd.Exercise.domain.field.entity.enums.Goal;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import com.dnd.Exercise.domain.field.entity.enums.Strength;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class FindAllFieldsCond {
    @ApiModelProperty(notes = "검색어")
    private String keyword;
    @ApiModelProperty(notes = "매칭유형", required = true, example = "DUEL | TEAM_BATTLE | TEAM")
    @NotNull
    private FieldType fieldType;
    @ApiModelProperty(notes = "팀 인원수 - 최소 1, 최대 10")
    @Range(min = 1, max = 10)
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
