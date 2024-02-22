package com.dnd.Exercise.domain.MemberEntry.controller;

import com.dnd.Exercise.domain.BattleEntry.dto.response.FindBattleEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.dtos.response.FindMemberEntriesRes;
import com.dnd.Exercise.domain.MemberEntry.service.MemberEntryService;
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

@Api(tags = "팀원 신청 내역 📬")
@RestController
@RequestMapping("/entry/member")
@RequiredArgsConstructor
public class MemberEntryController {

    private final MemberEntryService memberEntryService;

    @ApiOperation(value = "팀원 신청 📬")
    @ApiResponses({
            @ApiResponse(code=200, message="팀 신청 완료"),
            @ApiResponse(code=400, message="[F-008] 필드를 찾을 수 없습니다. "
                    + "<br>[F-004] 매치가 이미 진행 중입니다. "
                    + "<br>[FE-003] 이미 팀원이 가득 찼습니다. "
                    + "<br>[FE-001] 이미 신청한 필드입니다. "
                    + "<br>[FE-002] 이미 해당 유형의 매칭이 있습니다.")
    })
    @PostMapping("/apply")
    public ResponseEntity<String> createMemberEntry(
            @AuthenticationPrincipal User user,
            @RequestParam @NotNull Long fieldId){
        memberEntryService.createMemberEntry(user, fieldId);
        return ResponseDto.ok("팀 신청 완료");
    }

    @ApiOperation(value = "팀원 신청 취소 📬",
            notes = "EntryId를 입력받아 본인이 신청했던 팀원 신청 내역을 삭제한다")
    @ApiResponses({
            @ApiResponse(code=200, message="팀원 신청 취소 완료"),
            @ApiResponse(code=400, message="[F-008] 신청 내역을 찾을 수 없습니다."
                    + "<br>[C-000] 잘못된 요청"),
            @ApiResponse(code=403, message = "[FE-005] 매칭에 대한 설정은 팀장만 가능합니다.")
    })
    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<String> cancelMemberEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        memberEntryService.cancelMemberEntry(user, entryId);
        return ResponseDto.ok("팀원 신청 취소 완료");
    }

    @ApiOperation(value = "팀원 수락 📬")
    @ApiResponses({
            @ApiResponse(code=200, message="팀원 수락 완료"),
            @ApiResponse(code=400, message="[FE-005] 신청 내역을 찾을 수 없습니다. "
                    + "<br>[FE-003] 이미 팀원이 가득 찼습니다."),
            @ApiResponse(code=403, message = "[F-009] 팀장 권한이 필요합니다.")
    })
    @PostMapping("/accept/{id}")
    public ResponseEntity<String> acceptMemberEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        memberEntryService.acceptMemberEntry(user, entryId);
        return ResponseDto.ok("필드 수락 완료");
    }

    @ApiOperation(value = "[팀 - 팀원] 페이지 - 팀원 신청받은 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @ApiResponses({
            @ApiResponse(code=403, message="[F-012] 팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="[F-008] 필드를 찾을 수 없습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FindMemberEntriesRes> findReceivedMemberEntries(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        FindMemberEntriesRes result = memberEntryService.findReceivedMemberEntries(user, fieldId, pageable);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 팀원 신청한 내역 조회 📬",
            notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping
    public ResponseEntity<FindBattleEntriesRes> findMySentMemberEntries(
            @AuthenticationPrincipal User user,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        FindBattleEntriesRes result = memberEntryService.findMySentMemberEntries(user, pageable);
        return ResponseDto.ok(result);
    }
}
