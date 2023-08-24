package com.dnd.Exercise.domain.exercise.dto.request;

import com.dnd.Exercise.domain.sports.entity.Sports;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UpdateExerciseReq {
    @NotNull(message = "운동 종목을 입력해주세요.")
    private Sports sports;
    @NotNull(message = "운동 날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate exerciseDate;
    @NotNull(message = "운동 시간을 입력해주세요.")
    @ApiModelProperty(value = "운동시간 (분 단위)")
    private int durationMinute;
    @NotNull(message = "소모 칼로리를 입력해주세요.")
    private int burnedCalorie;

    @NotNull
    @ApiModelProperty(value = "기존 이미지 삭제 여부 (유저가 이미지 카드 UI의 x표시를 클릭해 기존 이미지 제거 시 true 로 설정. 디폴트값은 false)")
    private Boolean deletePrevImg;
    @ApiModelProperty(value = "새로 업로드 하고자 하는 이미지파일")
    private MultipartFile newMemoImgFile;
    private String memoContent;
    private Boolean isMemoPublic;
}
