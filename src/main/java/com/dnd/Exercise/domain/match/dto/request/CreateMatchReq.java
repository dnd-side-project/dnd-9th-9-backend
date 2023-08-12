package com.dnd.Exercise.domain.match.dto.request;

import com.dnd.Exercise.domain.match.entity.Goal;
import com.dnd.Exercise.domain.match.entity.MatchType;
import com.dnd.Exercise.domain.match.entity.Period;
import com.dnd.Exercise.domain.match.entity.Strength;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class CreateMatchReq {

    @NotBlank
    private String name;

    private String profile_img;

    @NotNull
    private Strength strength;

    @NotNull
    private Goal goal;

    private String rule;

    @NotNull
    private int maxSize;

    @NotNull
    private Period period;

    private String description;

    @NotNull
    private MatchType matchType;
}

