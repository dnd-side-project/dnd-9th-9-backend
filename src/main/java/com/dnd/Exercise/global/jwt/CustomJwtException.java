package com.dnd.Exercise.global.jwt;

import com.dnd.Exercise.global.error.dto.ErrorCode;
import com.dnd.Exercise.global.error.exception.BusinessException;
import lombok.Getter;

@Getter
public class CustomJwtException extends BusinessException {
    public CustomJwtException(ErrorCode errorCode) {
        super(errorCode);
    }
}
