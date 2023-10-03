package com.dnd.Exercise.global.jwt;

import com.dnd.Exercise.global.error.dto.ErrorCode;
import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String exception = (String)request.getAttribute("exception");

        if (exception == null) {
            setErrorResponse(response, ErrorCode.UNAUTHORIZED);
        }
        else if (exception.equals(ErrorCode.INVALID_JWT_TOKEN.getCode())) {
            setErrorResponse(response, ErrorCode.INVALID_JWT_TOKEN);
        }
        else if (exception.equals(ErrorCode.EXPIRED_JWT_TOKEN.getCode())) {
            setErrorResponse(response, ErrorCode.EXPIRED_JWT_TOKEN);
        }
        else if (exception.equals(ErrorCode.UNSUPPORTED_JWT_TOKEN.getCode())) {
            setErrorResponse(response, ErrorCode.UNSUPPORTED_JWT_TOKEN);
        }
        else {
            setErrorResponse(response, ErrorCode.UNAUTHORIZED);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getMessage());
        responseJson.put("code", errorCode.getCode());

        response.getWriter().print(responseJson);
    }
}
