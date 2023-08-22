package com.dnd.Exercise.domain.field.controller;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.FieldSideDateReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.field.service.FieldService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.time.LocalDate;
import java.util.List;
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
        fieldService.createField(createFieldReq, user);
        return ResponseDto.ok("필드 생성 완료");
    }


    @ApiOperation(value = "조건에 따른 모든 필드 조회 🔥",
            notes = "페이지 기본값: 0, 사이즈 기본값: 10 <br> Swagger의 page 관련 "
                    + "request 인자(offset, pageNumber, pageSize, paged, sort.sorted, sort.unsorted, unpaged)는 "
                    + "배제하고 page, size만 넣으면 페이징됩니다")
    @GetMapping
    public ResponseEntity<FindAllFieldsRes> findAllFields(
            @ModelAttribute FindAllFieldsCond findAllFieldsCond,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        FindAllFieldsRes result = fieldService.findAllFields(findAllFieldsCond, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "단일 필드 조회 🔥", notes = "팀원 정보를 제외한 해당 필드에 관한 정보를 불러옵니다. <br>"
            + "로그인한 유저가 해당 필드의 팀원이고, 매칭된 필드일 경우 상대 팀 정보를 추가로 조회합니다")
    @GetMapping("/{id}")
    public ResponseEntity<FindFieldRes> findField(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        FindFieldRes result = fieldService.findField(id, user);
        return ResponseDto.ok(result);
    }



    @ApiOperation(value = "필드 프로필 수정 🔥")
    @PatchMapping("/{id}/profile")
    public ResponseEntity<String> updateFieldProfile(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFieldProfileReq updateFieldProfileReq){
        fieldService.updateFieldProfile(id, user, updateFieldProfileReq);
        return ResponseDto.ok("필드 프로필 수정 완료");
    }


    @ApiOperation(value = "필드 정보 수정 🔥")
    @PatchMapping("/{id}/info")
    public ResponseEntity<String> updateFieldInfo(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFieldInfoReq updateFieldInfoReq){
        fieldService.updateFieldInfo(id, user, updateFieldInfoReq);
        return ResponseDto.ok("필드 정보 수정 완료");
    }


    @ApiOperation(value = "필드 삭제 🔥")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteField(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        fieldService.deleteFieldId(id, user);
        return ResponseDto.ok("필드 삭제 완료");
    }

    @ApiOperation(value = "자동 매칭 🔥")
    @GetMapping("/auto")
    public ResponseEntity<AutoMatchingRes> autoMatching(
            @AuthenticationPrincipal User user,
            @Parameter(description = "1대1 배틀일 경우 DUEL, 팀 배틀일 경우 TEAM_BATTLE")
            @RequestParam("fieldType") FieldType fieldType){
        AutoMatchingRes result = fieldService.autoMatching(fieldType, user);
        return ResponseDto.ok(result);
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

    //  양방향 매핑 고려
    @ApiOperation(value = " (대결 지표로 사용되는) 나의 필드 or 상대편 필드 하루 요약 조회 🔥",
            notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br>"
                    + "우리팀 요약: HOME, 상대팀 요약: AWAY <br>'하루 요약'에서 사용 <br>"
                    + "배틀 상대가 있는 필드로 HOME 조회 시 나의 승리 여부와 상대 필드 이름도 조회됩니다.")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/rating-summary")
    public ResponseEntity<GetFieldExerciseSummaryRes> getFieldExerciseSummary (
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @ModelAttribute FieldSideDateReq summaryReq) {
        GetFieldExerciseSummaryRes result = fieldService.getFieldExerciseSummary(user, fieldId, summaryReq);
        return ResponseDto.ok(result);
    }

    // DB에서 RANK 사용해서 상위 3개만 추출
    @ApiOperation(value = "나의 필드 or 상대편 필드 팀원별 랭킹 조회 🔥", notes = "팀과 팀배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/team/ranking")
    public ResponseEntity<GetRankingRes> getTeamRanking(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @ModelAttribute FieldSideDateReq teamRankingReq){
        GetRankingRes result = fieldService.getTeamRanking(user, fieldId, teamRankingReq);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "1:1 배틀 랭킹 조회 🔥", notes = "1:1 배틀에서만 사용")
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/duel/ranking")
    public ResponseEntity<GetRankingRes> getDuelRanking(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date){
        GetRankingRes result = fieldService.getDuelRanking(user, fieldId, date);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 리스트 조회",
            notes = " page 기본값: 0, size 기본값: 3")
    @GetMapping("{id}/record")
    public ResponseEntity<List<FindFieldRecordDto>> findAllFieldRecords(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @ModelAttribute FindAllFieldRecordsReq recordsReq){
        List<FindFieldRecordDto> result = fieldService.findAllFieldRecords(user, fieldId, recordsReq);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 단일 운동 조회", notes = "운동 내역 클릭시")
    @GetMapping("{fieldId}/record/{exerciseId}")
    public ResponseEntity<FindFieldRecordDto> findFieldRecord(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("fieldId") Long fieldId,
            @Parameter(description = "운동 Id값") @PathVariable("exerciseId") Long exerciseId){
        FindFieldRecordDto result = fieldService.findFieldRecord(user, fieldId, exerciseId);
        return ResponseDto.ok(result);
    }


    //매치 종료 스케줄러
    //매치 종료 Get
    //매치 상태값 Get
}
