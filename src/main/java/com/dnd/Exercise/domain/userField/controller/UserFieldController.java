package com.dnd.Exercise.domain.userField.controller;

import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsDto;
import com.dnd.Exercise.domain.field.dto.response.FindAllFieldsRes;
import com.dnd.Exercise.domain.field.entity.FieldType;
import com.dnd.Exercise.domain.userField.dto.response.FindAllMembersRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyBattleStatusRes;
import com.dnd.Exercise.domain.userField.dto.response.FindMyTeamStatusRes;
import com.dnd.Exercise.global.common.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "팀원 관련 정보, 필드 이력 관리 📜")
@RestController
@RequestMapping("/user-field")
public class UserFieldController {

    @ApiOperation(value = "팀원 리스트 조회 📜")
    @GetMapping("/{id}")
    public ResponseEntity<List<FindAllMembersRes>> findAllMembers(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId){
        List<FindAllMembersRes> findAllMembersRes = new ArrayList<FindAllMembersRes>();
        return ResponseDto.ok(findAllMembersRes);
    }

    @ApiOperation(value = "진행 중인 나의 필드 조회 📜", notes = "진행완료를 제외한 속해있는 모든 필드 조회")
    @GetMapping("/progress")
    public ResponseEntity<List<FindAllFieldsDto>> findAllMyInProgressFields(){
        List<FindAllFieldsDto> findAllMyInProgressFieldsRes = new ArrayList<FindAllFieldsDto>();
        return ResponseDto.ok(findAllMyInProgressFieldsRes);
    }

    @ApiOperation(value = "종료된 나의 필드 조회 📜", notes = "페이지 기본값: 0, 사이즈 기본값: 5")
    @GetMapping("/completed")
    public ResponseEntity<FindAllFieldsRes> findAllMyCompletedFields(
            @RequestParam(value = "fieldType") FieldType fieldType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size){
        FindAllFieldsRes findAllMyCompletedFieldsRes = new FindAllFieldsRes();
        return ResponseDto.ok(findAllMyCompletedFieldsRes);
    }

    @ApiOperation(value = "팀원 내보내기 📜")
    @DeleteMapping("/{id}/eject")
    public ResponseEntity<String> ejectMember(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long fieldId,
            @Parameter(description = "내보낼 팀원 ID 리스트") @RequestParam("ids") List<Long> ids){
        return ResponseDto.ok("팀원 내보내기 완료");
    }

    @ApiOperation(value = "필드 나가기 📜")
    @DeleteMapping("{id}/exit")
    public ResponseEntity<String> exitField(
            @Parameter(description = "필드 Id값") @PathVariable("id") Long id){
        return ResponseDto.ok("필드 나가기 완료");
    }

    @ApiOperation(value = "홈화면 나의 배틀 현황 조회 (팀 제외) 📜", notes = "데이터: 매치 시작일부터 특정 날짜까지의 누적 데이터")
    @GetMapping("/home/battle")
    public ResponseEntity<FindMyBattleStatusRes> findMyBattleStatus(){
        FindMyBattleStatusRes findMyBattleStatusRes = new FindMyBattleStatusRes();
        return ResponseDto.ok(findMyBattleStatusRes);
    }

    @ApiOperation(value = "홈화면 나의 팀 현황 조회 (팀배틀, 1:1 배틀 제외)", notes = "데이터: 매치 시작일부터 오늘까지의 누적 데이터")
    @GetMapping("/home/team")
    public ResponseEntity<FindMyTeamStatusRes> findMyTeamStatus(){
        FindMyTeamStatusRes findMyTeamStatusRes = new FindMyTeamStatusRes();
        return ResponseDto.ok(findMyTeamStatusRes);
    }
}
