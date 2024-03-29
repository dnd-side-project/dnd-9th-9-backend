package com.dnd.Exercise.domain.field.controller;

import com.dnd.Exercise.domain.field.dto.request.CreateFieldReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldRecordsReq;
import com.dnd.Exercise.domain.field.dto.request.FindAllFieldsCond;
import com.dnd.Exercise.domain.field.dto.request.FieldSideDateReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldInfoReq;
import com.dnd.Exercise.domain.field.dto.request.UpdateFieldProfileReq;
import com.dnd.Exercise.domain.field.dto.response.AutoMatchingRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldRecordsRes;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRecordDto;
import com.dnd.Exercise.domain.field.dto.response.FindFieldRes;
import com.dnd.Exercise.domain.field.dto.response.FindFieldResultRes;
import com.dnd.Exercise.domain.field.dto.response.GetFieldExerciseSummaryRes;
import com.dnd.Exercise.domain.field.dto.response.GetRankingRes;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.field.service.FieldService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.scheduling.annotation.Scheduled;
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

    @ApiOperation(value = "필드 생성 🔥")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 생성 완료"),
            @ApiResponse(code=400, message="[FE-002] 이미 해당 유형의 매칭이 있습니다."
                    + "<br>[F-010] 1:1 배틀의 최대 인원은 1명입니다. ")
    })
    @PostMapping
    public ResponseEntity<Long> createField(
            @AuthenticationPrincipal User user,
            @ModelAttribute @Valid CreateFieldReq createFieldReq){
        Long fieldId = fieldService.createField(createFieldReq, user);
        return ResponseDto.ok(fieldId);
    }


    @ApiOperation(value = "조건에 따른 모든 필드 조회 및 검색 🔥",
            notes = "페이지 기본값: 0, 사이즈 기본값: 10 <br> Swagger의 page 관련 "
                    + "request 인자(offset, pageNumber, pageSize, paged, sort.sorted, sort.unsorted, unpaged)는 "
                    + "배제하고 page, size만 넣으면 페이징됩니다")
    @GetMapping
    public ResponseEntity<FindAllFieldsRes> findAllFields(
            @ModelAttribute @Valid FindAllFieldsCond findAllFieldsCond){
        FindAllFieldsRes result = fieldService.findAllFields(findAllFieldsCond);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "조건에 따른 필드수 조회 🔥",
            notes = "필드 총 개수 Count API (페이지 이동 시 최초 1회만 조회) <br> "
                    + "fieldId는 null 값을 넣으면 됩니다. 이 외 항목들은 [필드 조회 및 검색] API 의 값들과 똑같이 채우면 됩니다.")
    @GetMapping("/count")
    public ResponseEntity<Long> countAllFields(
            @ModelAttribute @Valid FindAllFieldsCond findAllFieldsCond){
        Long result = fieldService.countAllFields(findAllFieldsCond);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "단일 필드 조회 🔥", notes = "팀원 정보를 제외한 해당 필드에 관한 정보를 불러옵니다. <br>"
            + "로그인한 유저가 해당 필드의 팀원이고, 매칭된 필드일 경우 상대 팀 정보를 추가로 조회합니다")
    @ApiResponses({
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FindFieldRes> findField(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        FindFieldRes result = fieldService.findField(id, user);
        return ResponseDto.ok(result);
    }



    @ApiOperation(value = "필드 프로필 수정 🔥")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 프로필 수정 완료"),
            @ApiResponse(code=400, message="[F-004] 매치가 이미 진행 중입니다. "
                    + "<br>[S-002] 파일 삭제 중 오류가 발생했습니다. "
                    + "<br>[S-001] 업로드 중 오류가 발생했습니다."),
            @ApiResponse(code=403, message="[F-009] 팀장 권한이 필요합니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @PostMapping("/{id}/profile")
    public ResponseEntity<String> updateFieldProfile(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @ModelAttribute @Valid UpdateFieldProfileReq updateFieldProfileReq){
        fieldService.updateFieldProfile(id, user, updateFieldProfileReq);
        return ResponseDto.ok("필드 프로필 수정 완료");
    }


    @ApiOperation(value = "필드 정보 수정 🔥")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 정보 수정 완료"),
            @ApiResponse(code=400, message="[F-004] 매치가 이미 진행 중입니다. "
                    + "<br>[F-010] 1:1 배틀의 최대 인원은 1명입니다."),
            @ApiResponse(code=403, message="[F-009] 팀장 권한이 필요합니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @PatchMapping("/{id}/info")
    public ResponseEntity<String> updateFieldInfo(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id,
            @RequestBody @Valid UpdateFieldInfoReq updateFieldInfoReq){
        fieldService.updateFieldInfo(id, user, updateFieldInfoReq);
        return ResponseDto.ok("필드 정보 수정 완료");
    }


    @ApiOperation(value = "필드 삭제 🔥")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 삭제 완료"),
            @ApiResponse(code=400, message="[F-002] 완료된 필드에 대해서는 삭제가 불가능합니다. "
                    + "<br>[F-004] 매치가 이미 진행 중입니다."),
            @ApiResponse(code=403, message="[F-009] 팀장 권한이 필요합니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteField(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        fieldService.deleteField(id, user);
        return ResponseDto.ok("필드 삭제 완료");
    }


    @ApiOperation(value = "자동 매칭 🔥")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-004] 매치가 이미 진행 중입니다. "
                    + "<br>[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-009] 팀장 권한이 필요합니다."),
            @ApiResponse(code=404, message="[F-003] 매칭을 위해서는 해당 유형의 필드가 필요합니다. "
                    + "<br>[F-006] 비슷한 조건의 필드가 없습니다.")
    })
    @GetMapping("/auto")
    public ResponseEntity<AutoMatchingRes> autoMatching(
            @AuthenticationPrincipal User user,
            @Parameter(description = "1대1 배틀일 경우 DUEL, 팀 배틀일 경우 TEAM_BATTLE")
            @RequestParam("fieldType") FieldType fieldType){
        AutoMatchingRes result = fieldService.autoMatching(fieldType, user);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "방장 넘기기 🔥")
    @ApiResponses({
        @ApiResponse(code=200, message="팀장 변경 완료"),
        @ApiResponse(code=403, message="[F-009] 팀장 권한이 필요합니다. "
                + "<br>[F-012] 팀 멤버가 아닙니다."),
        @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @PatchMapping("/{id}/change-leader")
    public ResponseEntity<String> changeLeader(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @Parameter(description = "새로운 리더 Id값") @RequestParam("id") Long id){
        fieldService.changeLeader(user, fieldId, id);
        return ResponseDto.ok("팀장 변경 완료");
    }

    @ApiOperation(value = " 대결 지표로 사용되는 나의 필드 하루 요약 조회 🔥",
            notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br>"
                    + "'하루 요약'에서 사용 <br>"
                    + "배틀 상대가 있는 필드로 조회 시 나의 승리 여부와 상대 필드 이름도 조회됩니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008]필드를 찾을 수 없습니다. ")
    })
    @GetMapping("/{id}/rating-summary/mine")
    public ResponseEntity<GetFieldExerciseSummaryRes> getMyFieldExerciseSummary (
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date) {
        GetFieldExerciseSummaryRes result = fieldService.getMyFieldExerciseSummary(user, fieldId, date);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = " 대결 지표로 사용되는 상대편 필드 하루 요약 조회 🔥",
            notes = "특정 하루에 대한 [기록횟수, 오늘까지의 활동링 달성 횟수, 운동시간, 소모 칼로리] 정보 조회 <br>"
                    + "'상대팀 진행 현황'에서 사용")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008]필드를 찾을 수 없습니다. "
                    + "<br>[F-007] 매칭된 상대 필드가 없습니다.")
    })
    @GetMapping("/{id}/rating-summary/opponent")
    public ResponseEntity<GetFieldExerciseSummaryRes> getOpponentFieldExerciseSummary (
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date) {
        GetFieldExerciseSummaryRes result = fieldService.getOpponentFieldExerciseSummary(user, fieldId, date);
        return ResponseDto.ok(result);
    }


    // DB에서 RANK 사용해서 상위 3개만 추출
    @ApiOperation(value = "나의 필드 or 상대편 필드 팀원별 랭킹 조회 🔥", notes = "팀과 팀배틀에서만 사용")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/{id}/team/ranking")
    public ResponseEntity<GetRankingRes> getTeamRanking(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @ModelAttribute @Valid FieldSideDateReq teamRankingReq){
        GetRankingRes result = fieldService.getTeamRanking(user, fieldId, teamRankingReq);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "1:1 배틀 랭킹 조회 🔥", notes = "1:1 배틀에서만 사용")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @ApiImplicitParam(name = "date", value = "선택 날짜", required = true, dataType = "string")
    @GetMapping("/{id}/duel/ranking")
    public ResponseEntity<GetRankingRes> getDuelRanking(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate date){
        GetRankingRes result = fieldService.getDuelRanking(user, fieldId, date);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 리스트 조회 🔥",
            notes = " page 기본값: 0, size 기본값: 3 <br> DUEL 일 경우 상대방 기록까지 조회")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("{id}/record")
    public ResponseEntity<FindAllFieldRecordsRes> findAllFieldRecords(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @ModelAttribute @Valid FindAllFieldRecordsReq recordsReq){
        FindAllFieldRecordsRes result = fieldService.findAllFieldRecords(user, fieldId, recordsReq);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[필드 - 기록] 페이지 스레드 단일 운동 조회 🔥", notes = "운동 내역 클릭시")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-005] 현재 팀원 모집 중입니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다. "
                    + "<br>[E-001] 운동 정보를 찾을 수 없습니다.")
    })
    @GetMapping("{fieldId}/record/{exerciseId}")
    public ResponseEntity<FindFieldRecordDto> findFieldRecord(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("fieldId") Long fieldId,
            @Parameter(description = "운동 Id값") @PathVariable("exerciseId") Long exerciseId){
        FindFieldRecordDto result = fieldService.findFieldRecord(user, fieldId, exerciseId);
        return ResponseDto.ok(result);
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void checkFieldStatus(){
        fieldService.checkFieldStatus();
    }


    @ApiOperation(value = "[필드 - 매칭] 종료된 필드 매칭 결과 조회 🔥"
            , notes = "매칭 결과, 점수 분석, 받은 뱃지를 반환합니다.")
    @ApiResponses({
            @ApiResponse(code=400, message="[F-011] 완료된 필드가 아닙니다."),
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("{id}/result")
    public ResponseEntity<FindFieldResultRes> findFieldResult(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId){
        FindFieldResultRes result = fieldService.findFieldResult(user, fieldId);
        return ResponseDto.ok(result);
    }
}
