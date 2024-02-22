package com.dnd.Exercise.domain.BattleEntry.controller;

import com.dnd.Exercise.domain.field.entity.enums.BattleType;
import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.BattleEntry.service.BattleEntryService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "배틀 신청 내역 📬")
@RestController
@RequestMapping("/entry/battle")
@RequiredArgsConstructor
public class BattleEntryController {

    private final BattleEntryService battleEntryService;

    @ApiOperation(value = "1:1 배틀 또는 팀 배틀 신청 📬", notes = "필드 -> 필드")
    @ApiResponses({
            @ApiResponse(code=200, message="배틀 신청 완료"),
            @ApiResponse(code=400, message="[F-008] 필드를 찾을 수 없습니다. "
                    + "<br>[F-004] 매치가 이미 진행 중입니다. "
                    + "<br>[FE-001] 이미 신청한 필드입니다. "
                    + "<br>[F-003] 매칭을 위해서는 해당 유형의 필드가 필요합니다."
                    + "<br>[F-005] 현재 팀원 모집 중입니다."
                    + "<br>[FE-004] 기간이 같아야 합니다."
                    + "<br>[C-000] 잘못된 요청 - 나의 필드에 배틀 신청을 했을 경우"),
            @ApiResponse(code=403, message = "팀장 권한이 필요합니다.")
    })
    @PostMapping("/apply/{id}")
    public ResponseEntity<String> createBattleEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "FieldId값") @PathVariable("id") Long fieldId){
        battleEntryService.createBattleEntry(user, fieldId);
        return ResponseDto.ok("배틀 신청 완료");
    }


    @ApiOperation(value = "배틀 신청 취소 📬",
            notes = "EntryId를 입력받아 신청했던 배틀 신청 내역을 삭제한다")
    @ApiResponses({
            @ApiResponse(code=200, message="배틀 신청 취소 완료"),
            @ApiResponse(code=400, message="[F-008] 필드를 찾을 수 없습니다."
                    + "<br>[C-000] 잘못된 요청"),
            @ApiResponse(code=403, message = "[F-009] 팀장 권한이 필요합니다.")
    })
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBattleEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        battleEntryService.cancelBattleEntry(user, entryId);
        return ResponseDto.ok("배틀 신청 취소 완료");
    }


    @ApiOperation(value = "배틀 수락 📬")
    @ApiResponses({
            @ApiResponse(code=200, message="배틀 수락 완료"),
            @ApiResponse(code=400, message="[FE-005] 신청 내역을 찾을 수 없습니다. "
                    + "<br>[FE-003] 이미 팀원이 가득 찼습니다."),
            @ApiResponse(code=403, message = "[F-009] 팀장 권한이 필요합니다.")
    })
    @PostMapping("/accept/{id}")
    public ResponseEntity<String> acceptBattleEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        battleEntryService.acceptBattleEntry(user, entryId);
        return ResponseDto.ok("필드 수락 완료");
    }

    @ApiOperation(value = "[매치 - 매칭] 페이지 - 배틀 신청한 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @ApiResponses({
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/sent/{id}")
    public ResponseEntity<FindBattleEntriesRes> findSentBattleEntries(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        FindBattleEntriesRes result = battleEntryService.findSentBattleEntries(user, fieldId, pageable);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[매치 - 매칭] 페이지 - 배틀 신청 받은 내역 조회 📬",
            notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @ApiResponses({
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/received/{id}")
    public ResponseEntity<FindBattleEntriesRes> findReceivedBattleEntries(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        FindBattleEntriesRes result = battleEntryService.findReceivedBattleEntries(user, fieldId, pageable);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 배틀 신청한 내역 조회 (팀 배틀, 1:1 배틀) 📬",
            notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping("/sent")
    public ResponseEntity<FindBattleEntriesRes> findMySentBattleEntriesByType(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "fieldType") BattleType battleType,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        FindBattleEntriesRes result = battleEntryService.findMySentBattleEntries(user, battleType, pageable);
        return ResponseDto.ok(result);
    }
}
