package com.dnd.Exercise.domain.user.dto.request;

import com.dnd.Exercise.domain.field.entity.enums.SkillLevel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMyProfileReq {
    private String name;

    @NotNull
    @ApiModelProperty(value = "기존 프로필 이미지 삭제 여부 (유저가 이미지 카드 UI의 x표시를 클릭해 기존 이미지 제거 시 true 로 설정. 디폴트값은 false)")
    private Boolean deletePrevImg;

    @ApiModelProperty(value = "새로 업로드 하고자 하는 프로필 이미지")
    private MultipartFile newProfileImg;

    private Integer age;

    private Double height;

    private Double weight;

    private SkillLevel skillLevel;
}
