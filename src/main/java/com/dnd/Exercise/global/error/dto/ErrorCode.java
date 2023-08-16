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

    INVALID_STATUS(HttpStatus.BAD_REQUEST, "F-001", "진행 중, 완료된 필드에 대해서는 수정이 불가능합니다."),

    DELETE_FAILED(HttpStatus.BAD_REQUEST, "F-002", "완료된 필드에 대해서는 삭제가 불가능합니다."),

    SHOULD_CREATE(HttpStatus.NOT_FOUND, "F-003", "자동매칭을 위해서는 해당 유형의 필드가 필요합니다."),

    ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "F-004", "매치가 이미 진행 중입니다."),

    RECRUITING_YET(HttpStatus.BAD_REQUEST, "F-005", "현재 팀원 모집 중입니다."),

    NO_SIMILAR_FIELD_FOUND(HttpStatus.NOT_FOUND, "F-006", "비슷한 조건의 필드가 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
