package com.dnd.Exercise.domain.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAppleLinkedReq {
    private Boolean isAppleLinked;
}
