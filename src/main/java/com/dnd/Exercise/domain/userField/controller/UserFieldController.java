package com.dnd.Exercise.domain.userField.controller;

import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.entity.BattleType;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.user.entity.User;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.domain.userField.service.UserFieldService;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "팀원 관련 정보, 필드 이력 관리 📜")
@RestController
@RequestMapping("/user-field")
@RequiredArgsConstructor
public class UserFieldController {

    private final UserFieldService userFieldService;

    @ApiOperation(value = "팀원 리스트 조회 📜")
    @ApiResponses({
            @ApiResponse(code=404, message="필드를 찾을 수 없습니다.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<FindAllMembersRes>> findAllMembers(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId){
        List<FindAllMembersRes> findAllMembersRes = userFieldService.findAllMembers(fieldId);
        return ResponseDto.ok(findAllMembersRes);
    }

    @ApiOperation(value = "진행 중인 나의 필드 조회 📜", notes = "진행완료를 제외한 속해있는 모든 필드 조회")
    @GetMapping("/progress")
    public ResponseEntity<List<FindAllFieldsDto>> findAllMyInProgressFields(
            @AuthenticationPrincipal User user){
        List<FindAllFieldsDto> result = userFieldService.findAllMyInProgressFields(user);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "종료된 나의 필드 조회 📜",
            notes = "페이지 기본값: 0, 사이즈 기본값: 5, fieldType = null 일 경우 전체 조회")
    @GetMapping("/completed")
    public ResponseEntity<List<FindAllFieldsDto>> findAllMyCompletedFields(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "fieldType", required = false) FieldType fieldType,
            @PageableDefault(page = 0, size = 5) Pageable pageable){
        List<FindAllFieldsDto> result = userFieldService.findAllMyCompletedFields(user, fieldType, pageable);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "팀원 내보내기 📜")
    @DeleteMapping("/{id}/eject")
    public ResponseEntity<String> ejectMember(
            @AuthenticationPrincipal User user,
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @Parameter(description = "내보낼 팀원 ID 리스트") @RequestParam("ids") List<Long> ids){
        userFieldService.ejectMember(user, fieldId, ids);
        return ResponseDto.ok("팀원 내보내기 완료");
    }

    @ApiOperation(value = "필드 나가기 📜")
    @DeleteMapping("{id}/exit")
    public ResponseEntity<String> exitField(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        return ResponseDto.ok("필드 나가기 완료");
    }

    @ApiOperation(value = "홈화면 나의 배틀 현황 조회 (팀 제외) 📜",
            notes = "데이터: 매치 시작일부터 특정 날짜까지의 누적 데이터 <br> "
                    + "진행 중인 배틀이 없을 경우 null 이 반환됩니다.")
    @GetMapping("/home/battle")
    public ResponseEntity<FindMyBattleStatusRes> findMyBattleStatus(
            @AuthenticationPrincipal User user,
            @RequestParam BattleType battleType){
        FindMyBattleStatusRes result = userFieldService.findMyBattleStatus(user, battleType);
        return ResponseDto.ok(result);
    }

    @ApiOperation(value = "홈화면 나의 팀 현황 조회 (팀배틀, 1:1 배틀 제외) 📜",
            notes = "데이터: 매치 시작일부터 오늘까지의 누적 데이터 <br> "
                    + "진행 중인 팀이 없을 경우 null 이 반환됩니다.")
    @GetMapping("/home/team")
    public ResponseEntity<FindMyTeamStatusRes> findMyTeamStatus(
            @AuthenticationPrincipal User user){
        FindMyTeamStatusRes result = userFieldService.findMyTeamStatus(user);
        return ResponseDto.ok(result);
    }
}
