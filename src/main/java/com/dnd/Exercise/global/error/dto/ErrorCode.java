package com.dnd.Exercise.global.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-0000", "잘못된 요청"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "C-0001", "리소스를 찾을 수 없음"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C-0002", "허용되지 않은 Request Method 호출"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-0003", "내부 서버 오류"),

    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "C-0004", "요청 인자가 유효하지 않음"),

    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C005", "유효하지 않은 값 타입");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
