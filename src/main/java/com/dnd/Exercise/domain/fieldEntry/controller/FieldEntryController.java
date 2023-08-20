package com.dnd.Exercise.domain.fieldEntry.controller;

import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.fieldEntry.dto.request.BattleFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.TeamFieldEntryReq;
import com.dnd.Exercise.domain.fieldEntry.dto.request.FieldDirection;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllFieldEntryRes;
import com.dnd.Exercise.domain.fieldEntry.dto.response.FindAllTeamEntryRes;
import com.dnd.Exercise.domain.fieldEntry.service.FieldEntryService;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @GetMapping("/team/{id}")
    public ResponseEntity<FindAllTeamEntryRes> findAllTeamEntries(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllTeamEntryRes findAllTeamEntryRes = new FindAllTeamEntryRes();
        return ResponseDto.ok(findAllTeamEntryRes);
    }

    /**
     * AuthenticationPrinciple을 통한 본인이 참여하고 있는 필드인지 확인하는 로직 추가 고려
     */
    @ApiOperation(value = "[매치 - 매칭] 페이지 - 배틀 신청 내역 조회 📬",
            notes = "EntryDirection을 통해 요청 받은 내역과 요청한 내역 구분 <br> 페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping("/battle/{id}")
    public ResponseEntity<FindAllFieldEntryRes> findAllBattleEntriesByDirection(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @RequestParam(value = "fieldDirection") FieldDirection fieldDirection,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllFieldEntryRes findAllFieldEntryRes = new FindAllFieldEntryRes();
        return ResponseDto.ok(findAllFieldEntryRes);
    }


    @ApiOperation(value = "[My 매칭 - 신청] 페이지 - 필드 신청한 내역 조회 📬", notes = "페이지 기본값: 0, 사이즈 기본값: 3")
    @GetMapping
    public ResponseEntity<FindAllFieldEntryRes> findAllBattleEntriesByType(
            // AuthenticationPrinciple 추가
            @RequestParam(value = "fieldType") FieldType fieldType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size){
        FindAllFieldEntryRes findAllFieldEntryRes = new FindAllFieldEntryRes();
        return ResponseDto.ok(findAllFieldEntryRes);
    }


    @ApiOperation(value = "팀 또는 팀 배틀 입장 신청 📬", notes = "유저 -> 필드")
    @PostMapping("/team")
    public ResponseEntity<String> createTeamFieldEntry(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid TeamFieldEntryReq fieldEntryReq){
        fieldEntryService.createTeamFieldEntry(user, fieldEntryReq);
        return ResponseDto.ok("팀 신청 완료");
    }

    @ApiOperation(value = "1:1 배틀 또는 팀 배틀 신청 📬", notes = "필드 -> 필드")
    @PostMapping("/battle")
    public ResponseEntity<String> createBattleFieldEntry(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid BattleFieldEntryReq fieldEntryReq){
        fieldEntryService.createBattleFieldEntry(user, fieldEntryReq);
        return ResponseDto.ok("배틀 신청 완료");
    }

    @ApiOperation(value = "필드 신청 취소 📬",
            notes = "EntryId를 입력받아 본인이 신청했던 필드 신청 내역을 삭제한다")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFieldEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        fieldEntryService.deleteFieldEntry(user, entryId);
        return ResponseDto.ok("필드 신청 취소 완료");
    }

    @ApiOperation(value = "필드 수락 📬")
    @PostMapping("/{id}/accept")
    public ResponseEntity<String> acceptFieldEntry(
            @AuthenticationPrincipal User user,
            @Parameter(description = "EntryId값") @PathVariable("id") Long entryId){
        fieldEntryService.acceptFieldEntry(user, entryId);
        return ResponseDto.ok("필드 수락 완료");
    }
}
