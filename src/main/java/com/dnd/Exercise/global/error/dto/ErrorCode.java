package com.dnd.Exercise.global.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-000", "잘못된 요청"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "리소스를 찾을 수 없음"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C-002", "허용되지 않은 Request Method 호출"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-003", "내부 서버 오류"),

    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "C-004", "요청 인자가 유효하지 않음"),

    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C-005", "유효하지 않은 값 타입"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "C-006","접근 권한이 없습니다."),

    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "A-001", "아이디 또는 비밀번호를 잘못 입력하였습니다."),

    ID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "A-002", "이미 사용중인 아이디 입니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "A-003", "유효하지 않은 refresh 토큰 입니다."),

    INVALID_STATUS(HttpStatus.BAD_REQUEST, "F-001", "진행 중, 완료된 필드에 대해서는 수정이 불가능합니다."),

    DELETE_FAILED(HttpStatus.BAD_REQUEST, "F-002", "완료된 필드에 대해서는 삭제가 불가능합니다."),

    SHOULD_CREATE(HttpStatus.NOT_FOUND, "F-003", "매칭을 위해서는 해당 유형의 필드가 필요합니다."),

    ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "F-004", "매치가 이미 진행 중입니다."),

    RECRUITING_YET(HttpStatus.BAD_REQUEST, "F-005", "현재 팀원 모집 중입니다."),

    NO_SIMILAR_FIELD_FOUND(HttpStatus.NOT_FOUND, "F-006", "비슷한 조건의 필드가 없습니다."),

    OPPONENT_NOT_FOUND(HttpStatus.NOT_FOUND, "F-007", "매칭된 상대 필드가 없습니다."),

    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "F-008", "필드를 찾을 수 없습니다."),

    NOT_LEADER(HttpStatus.FORBIDDEN, "F-009", "팀장 권한이 필요합니다."),

    ALREADY_APPLY(HttpStatus.BAD_REQUEST, "FE-001", "이미 신청한 필드입니다."),

    HAVING_IN_PROGRESS(HttpStatus.BAD_REQUEST, "FE-002", "이미 해당 유형의 필드를 가지고 있습니다. "
            + "가질 수 있는 최대 필드 수 : 1:1 배틀 1개, 팀 배틀 1개, 팀 1개"),

    ALREADY_FULL(HttpStatus.BAD_REQUEST, "FE-003", "이미 팀원이 가득 찼습니다."),

    PERIOD_NOT_MATCH(HttpStatus.BAD_REQUEST, "FE-004", "기간이 같아야 합니다."),

    ACTIVITY_RING_NOT_FOUND(HttpStatus.NOT_FOUND, "AR-001", "해당 날짜에 대한 활동링 소모칼로리 정보가 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
