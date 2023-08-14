package com.dnd.Exercise.domain.field.controller;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldRecordsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.entity.FieldSide;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.service.FieldService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "필드 (팀, 팀배틀, 1:1 배틀) 🔥")
@Slf4j
@RestController
@RequestMapping("/field")
@RequiredArgsConstructor
public class FieldController {

    private final FieldService fieldService;

    @ApiOperation(value = "필드 생성 🔥", notes = "프로필 사진 업로드가 먼저 진행되어야 합니다")
    @PostMapping
    public ResponseEntity<String> createField(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateFieldReq createFieldReq){
        Long userId = user.getId();
        fieldService.createField(createFieldReq, userId);
        return ResponseDto.ok("필드 생성 완료");
    }


    @ApiOperation(value = "조건에 따른 모든 필드 조회 🔥", notes = "페이지 기본값: 0, 사이즈 기본값: 10")
    @GetMapping
    public ResponseEntity<FindAllFieldsRes> findAllFields(
            @ModelAttribute("findAllFieldsCond") FindAllFieldsCond findAllFieldsCond,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        FindAllFieldsRes findAllFieldsRes = new FindAllFieldsRes();
        return ResponseDto.ok(findAllFieldsRes);
    }


    @ApiOperation(value = "단일 필드 조회 🔥", notes = "팀원 제외, 해당 필드에 관한 정보와 매칭된 필드일 경우 상대 팀 정보를 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<FindFieldRes> findField(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        FindFieldRes findFieldRes = new FindFieldRes();
        return ResponseDto.ok(findFieldRes);
    }

    @ApiOperation(value = "필드 프로필 수정 🔥")
    @PatchMapping("/{id}/profile")
    public ResponseEntity<String> updateFieldProfile(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFieldProfileReq updateFieldProfileReq){
        return ResponseDto.ok("필드 프로필 수정 완료");
    }


    @ApiOperation(value = "필드 정보 수정 🔥")
    @PatchMapping("/{id}/info")
    public ResponseEntity<String> updateFieldInfo(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFieldInfoReq updateFieldInfoReq){
        return ResponseDto.ok("필드 정보 수정 완료");
    }


    @ApiOperation(value = "필드 삭제 🔥")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteField(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        return ResponseDto.ok("필드 삭제 완료");
    }

    @ApiOperation(value = "자동 매칭 🔥")
    @GetMapping("/auto")
    public ResponseEntity<AutoMatchingRes> autoFielding(
            @Parameter(description = "1대1 배틀일 경우 DUEL, 팀 배틀일 경우 TEAM_BATTLE")
            @RequestParam("fieldType") FieldType fieldType){
        AutoMatchingRes autoMatchingRes = new AutoMatchingRes();
        return ResponseDto.ok(autoMatchingRes);
    }

    @ApiOperation(value = "방장 넘기기 🔥")
    @PatchMapping("/{id}/change-leader")
    public ResponseEntity<String> changeLeader(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @Parameter(description = "새로운 리더 Id값") @RequestParam("id") Long id){
        return ResponseDto.ok("팀장 변경 완료");
    }

    @ApiOperation(value = "배틀 중단하기 🔥",
            notes = "배틀 중단 시 정책 결정(필드 삭제 혹은 필드 간의 연결만 끊기) <br> **Delete or Patch**")
    @DeleteMapping("/{id}/terminate")
    public ResponseEntity<String> terminateBattle(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId){
        return ResponseDto.ok("배틀 중단 완료");
    }

    // DB에서 SUM 연산해서 가져오기, 양방향 매핑 고려
    @ApiOperation(value = " (대결 지표로 사용되는) 나의 필드 or 상대편 필드 하루 요약 조회 🔥",
            notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br>"
                    + "우리팀 요약: HOME, 상대팀 요약: AWAY <br>'홈화면', '하루 요약'에서 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/rating-summary")
    public ResponseEntity<GetFieldExerciseSummaryRes> getFieldExerciseSummary (
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date,
            @RequestParam FieldSide fieldSide) {
        GetFieldExerciseSummaryRes getFieldExerciseSummaryRes = new GetFieldExerciseSummaryRes();
        return ResponseDto.ok(getFieldExerciseSummaryRes);
    }

    // DB에서 RANK 사용해서 상위 3개만 추출
    @ApiOperation(value = "나의 필드 or 상대편 필드 팀원별 랭킹 조회 🔥", notes = "팀과 팀배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/team/ranking")
    public ResponseEntity<GetRankingRes> getTeamRanking(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date,
            @RequestParam FieldSide fieldSide){
        GetRankingRes getTeamRankingRes = new GetRankingRes();
        return ResponseDto.ok(getTeamRankingRes);
    }

    @ApiOperation(value = "1:1 배틀 랭킹 조회 🔥", notes = "1:1 배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/duel/ranking")
    public ResponseEntity<GetRankingRes> getDuelRanking(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date){
        GetRankingRes getDuelRankingRes = new GetRankingRes();
        return ResponseDto.ok(getDuelRankingRes);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 리스트 조회")
    @GetMapping("{id}/record")
    public ResponseEntity<FindAllFieldRecordsRes> findAllFieldRecords(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @RequestBody FindAllFieldRecordsReq findAllFieldRecordsReq){
        FindAllFieldRecordsRes findAllFieldRecordsRes = new FindAllFieldRecordsRes();
        return ResponseDto.ok(findAllFieldRecordsRes);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 단일 운동 조회", notes = "운동 내역 클릭시")
    @GetMapping("{fieldId}/record/{exerciseId}")
    public ResponseEntity<FindFieldRecordDto> findFieldRecord(
            @Parameter(description = "필드 Id값") @PathVariable("fieldId") Long fieldId,
            @Parameter(description = "운동 Id값") @PathVariable("exerciseId") Long exerciseId){
        FindFieldRecordDto findFieldRecordDto = new FindFieldRecordDto();
        return ResponseDto.ok(findFieldRecordDto);
    }


    //매치 종료 스케줄러
    //매치 종료 Get
}
