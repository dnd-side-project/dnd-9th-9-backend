package com.dnd.Exercise.domain.fieldEntry.controller;

import com.dnd.Exercise.domain.field.entity.enums.FieldType;
import com.dnd.Exercise.domain.fieldEntry.dto.request.BattleFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.TeamFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllBattleEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.dnd.Exercise.domain.fieldEntry.service.FieldEntryService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "필드 신청 내역 📬")
@RestController
@RequestMapping("/field-entry")
@RequiredArgsConstructor
public class FieldEntryController {

    private final FieldEntryService fieldEntryService;

    @ApiOperation(value = "[팀 - 팀원] 페이지 - 팀 신청받은 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @ApiResponses({
            @ApiResponse(code=403, message="팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="필드를 찾을 수 없습니다.")
    })
    @GetMapping("/team/{id}")
    public ResponseEntity<List<FindAllTeamEntryRes>> findAllTeamEntries(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        List<FindAllTeamEntryRes> result = fieldEntryService.findAllTeamEntries(user, fieldId, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "[매치 - 매칭] 페이지 - 배틀 신청 내역 조회 📬",
            notes = "EntryDirection을 통해 요청 받은 내역과 요청한 내역 구분 <br> 페이지 기본값: 0, 사이즈 기본값: 3")
    @ApiResponses({
            @ApiResponse(code=403, message="팀 멤버가 아닙니다."),
            @ApiResponse(code=404, message="필드를 찾을 수 없습니다.")
    })
    @GetMapping("/battle/{id}")
    public ResponseEntity<List<FindAllBattleEntryRes>> findAllBattleEntriesByDirection(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @RequestParam(value = "fieldDirection") FieldDirection fieldDirection,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        List<FindAllBattleEntryRes> result = fieldEntryService.findAllBattleEntriesByDirection(user, fieldId, fieldDirection, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 필드 신청한 내역 조회 📬",
            notes = "페이지 기본값: 0, 사이즈 기본값: 3 <br> 신청한 내역이 없을 경우 null 을 반환합니다.")
    @GetMapping
    public ResponseEntity<List<FindAllBattleEntryRes>> findAllBattleEntriesByType(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "fieldType") FieldType fieldType,
            @PageableDefault(page = 0, size = 3) Pageable pageable){
        List<FindAllBattleEntryRes> result = fieldEntryService.findAllBattleEntriesByType(user, fieldType, pageable);
        return ResponseDto.ok(result);
    }


    @ApiOperation(value = "팀 또는 팀 배틀 입장 신청 📬", notes = "유저 -> 필드")
    @ApiResponses({
            @ApiResponse(code=200, message="팀 신청 완료"),
            @ApiResponse(code=400, message="필드를 찾을 수 없습니다. | 매치가 이미 진행 중입니다. "
                    + "| 이미 팀원이 가득 찼습니다. | 이미 신청한 필드입니다. "
                    + "| 이미 해당 유형의 필드를 가지고 있습니다. 가질 수 있는 최대 필드 수 : 1:1 배틀 1개, 팀 배틀 1개, 팀 1개")
    })
    @PostMapping("/team")
    public ResponseEntity<String> createTeamFieldEntry(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid TeamFieldEntryReq fieldEntryReq){
        fieldEntryService.createTeamFieldEntry(user, fieldEntryReq);
        return ResponseDto.ok("팀 신청 완료");
    }


    @ApiOperation(value = "1:1 배틀 또는 팀 배틀 신청 📬", notes = "필드 -> 필드")
    @ApiResponses({
            @ApiResponse(code=200, message="배틀 신청 완료"),
            @ApiResponse(code=400, message="필드를 찾을 수 없습니다. | 매치가 이미 진행 중입니다. "
                    + "| 이미 신청한 필드입니다. | 매칭을 위해서는 해당 유형의 필드가 필요합니다."
                    + "| 현재 팀원 모집 중입니다. | 기간이 같아야 합니다. | 잘못된 요청"),
            @ApiResponse(code=403, message = "팀장 권한이 필요합니다.")
    })
    @PostMapping("/battle")
    public ResponseEntity<String> createBattleFieldEntry(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid BattleFieldEntryReq fieldEntryReq){
        fieldEntryService.createBattleFieldEntry(user, fieldEntryReq);
        return ResponseDto.ok("배틀 신청 완료");
    }


    @ApiOperation(value = "필드 신청 취소 📬",
            notes = "EntryId를 입력받아 본인이 신청했던 필드 신청 내역을 삭제한다")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 신청 취소 완료"),
            @ApiResponse(code=400, message="필드를 찾을 수 없습니다. | 잘못된 요청"),
            @ApiResponse(code=403, message = "접근 권한이 없습니다.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFieldEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        fieldEntryService.deleteFieldEntry(user, entryId);
        return ResponseDto.ok("필드 신청 취소 완료");
    }


    @ApiOperation(value = "필드 수락 📬")
    @ApiResponses({
            @ApiResponse(code=200, message="필드 수락 완료"),
            @ApiResponse(code=400, message="필드를 찾을 수 없습니다. | 이미 팀원이 가득 찼습니다."),
            @ApiResponse(code=403, message = "팀장 권한이 필요합니다.")
    })
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptFieldEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        fieldEntryService.acceptFieldEntry(user, entryId);
        return ResponseDto.ok("필드 수락 완료");
    }
}
