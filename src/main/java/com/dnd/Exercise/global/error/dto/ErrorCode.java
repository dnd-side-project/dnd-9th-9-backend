package com.dnd.Exercise.global.error.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C-000", "잘못된 요청입니다\n다시 한 번 확인해주세요"),

    NOT_FOUND(HttpStatus.NOT_FOUND, "C-001", "리소스를 찾을 수 없음"),

    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C-002", "허용되지 않은 Request Method 호출"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C-003", "알 수 없는 오류가 발생하였습니다."),

    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "C-004", "요청 인자가 유효하지 않음"),

    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "C-005", "유효하지 않은 값 타입"),

    FORBIDDEN(HttpStatus.FORBIDDEN, "C-006","접근 권한이 없습니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C-007", "로그인이 필요합니다."),

    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J-001", "유효하지 않은 JWT 토큰입니다."),

    EXPIRED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J-002", "만료된 JWT 토큰입니다."),

    UNSUPPORTED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J-003", "지원하지 않는 JWT 토큰입니다."),

    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "A-001", "아이디 또는 비밀번호를 잘못 입력하였습니다."),

    ID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "A-002", "이미 사용중인 아이디 입니다."),

    UNMATCHING_NEW_PASSWORD(HttpStatus.BAD_REQUEST, "A-003", "비밀번호가 일치하지 않습니다."),

    INVALID_STATUS(HttpStatus.BAD_REQUEST, "F-001", "진행 중, 완료된 필드에 대해서는 수정이 불가능합니다."),

    DELETE_FAILED(HttpStatus.BAD_REQUEST, "F-002", "완료된 필드에 대해서는 삭제가 불가능합니다."),

    SHOULD_CREATE(HttpStatus.NOT_FOUND, "F-003", "매칭을 위해서는 해당 유형의 필드가 필요합니다."),

    ALREADY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "F-004", "매치가 이미 진행 중입니다."),

    RECRUITING_YET(HttpStatus.BAD_REQUEST, "F-005", "현재 팀원 모집 중입니다."),

    NO_SIMILAR_FIELD_FOUND(HttpStatus.NOT_FOUND, "F-006", "비슷한 조건의 필드가 없습니다."),

    OPPONENT_NOT_FOUND(HttpStatus.NOT_FOUND, "F-007", "매칭된 상대 필드가 없습니다."),

    FIELD_NOT_FOUND(HttpStatus.NOT_FOUND, "F-008", "필드를 찾을 수 없습니다."),

    NOT_LEADER(HttpStatus.FORBIDDEN, "F-009", "팀장 권한이 필요합니다."),

    DUEL_MAX_ONE(HttpStatus.BAD_REQUEST, "F-010", "1:1 배틀의 최대 인원은 1명입니다."),

    NOT_COMPLETED(HttpStatus.BAD_REQUEST, "F-011", "완료된 필드가 아닙니다."),

    NOT_MEMBER(HttpStatus.FORBIDDEN, "F-012", "팀 멤버가 아닙니다."),

    MUST_NOT_LEADER(HttpStatus.FORBIDDEN, "F-013", "팀을 나가기 위해서는\n다른 팀원에게 방장을 넘겨야 해요."),

    ALREADY_APPLY(HttpStatus.BAD_REQUEST, "FE-001", "이미 신청한 필드입니다."),

    HAVING_IN_PROGRESS(HttpStatus.BAD_REQUEST, "FE-002", "이미 해당 유형의 매칭이 있습니다."),

    ALREADY_FULL(HttpStatus.BAD_REQUEST, "FE-003", "이미 팀원이 가득 찼습니다."),

    PERIOD_NOT_MATCH(HttpStatus.BAD_REQUEST, "FE-004", "기간이 같아야 합니다."),

    MUST_LEADER(HttpStatus.FORBIDDEN, "FE-005", "매칭에 대한 설정은 팀장만 가능합니다."),

    ACTIVITY_RING_NOT_FOUND(HttpStatus.NOT_FOUND, "AR-001", "해당 날짜에 대한 활동링 소모칼로리 정보가 존재하지 않습니다."),

    ACTIVITY_RING_UPDATE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "AR-002", "애플 연동 유저만 활동링을 업데이트 할 수 있습니다."),

    EXERCISE_NOT_FOUND(HttpStatus.NOT_FOUND, "E-001", "운동 정보를 찾을 수 없습니다."),

    NEED_USER_WEIGHT_FOR_EXPECTED_CALORIE(HttpStatus.BAD_REQUEST,"E-002","예상 소모 칼로리 계산을 위해서는 몸무게가 사전 입력되어야 합니다."),

    APPLE_WORKOUTS_UPDATE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "E-003", "애플 연동을 수행한 유저만 애플 운동기록을 업로드 할 수 있습니다."),

    CALORIE_GOAL_UPDATE_UNAVAILABLE(HttpStatus.BAD_REQUEST, "U-001", "애플 연동을 수행한 유저만 목표 칼로리를 설정할 수 있습니다."),

    NEED_CALORIE_GOAL(HttpStatus.BAD_REQUEST, "U-002", "애플 연동 유저인 경우 목표 칼로리 값을 전송해야 합니다."),

    UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "S-001", "업로드 중 오류가 발생했습니다."),

    FILE_DELETE_FAILED(HttpStatus.BAD_REQUEST, "S-002", "파일 삭제 중 오류가 발생했습니다."),

    FCM_TIME_LIMIT(HttpStatus.BAD_REQUEST, "N-001", "2시간마다 가능합니다."),

    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "N-002", "알림을 찾을 수 없습니다."),

    INCORRECT_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "V-001", "잘못된 인증번호입니다."),

    EXPIRED_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "V-002", "인증번호 유효시간이 지났습니다."),

    UNEXISTING_USER(HttpStatus.BAD_REQUEST, "V-003", "해당 이름과 전화번호를 가진 유저는 존재하지 않습니다."),

    NEED_VERIFICATION(HttpStatus.BAD_REQUEST, "V-004", "전화번호 인증이 사전 수행되어야 합니다."),

    UNEXISTING_ID(HttpStatus.BAD_REQUEST, "V-005", "해당 아이디의 유저는 존재하지 않습니다."),

    UNMATCHING_PHONE_NUM(HttpStatus.BAD_REQUEST, "V-006", "회원정보에 등록된 전화번호와 일치하지 않습니다."),

    INVALID_KAKAO_TOKEN(HttpStatus.UNAUTHORIZED, "K-001", "해당 액세스 토큰으로 카카오 유저 정보를 받아오지 못했습니다."),

    TEAMWORK_RATE_POSTED_ALREADY(HttpStatus.BAD_REQUEST, "TR-001", "해당 유저는 이미 불꽃평가 등록을 완료했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
