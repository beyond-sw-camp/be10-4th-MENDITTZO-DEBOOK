package com.mendittzo.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

// 인가 실패 시 응답

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 접근 권한이 없은데 접근한 경우
        // 인가 실패로 403 오류 응답
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");  // todo: 에러 코드 json으로 넘겨야 하나?
        response.getWriter().write(new ObjectMapper().writeValueAsString(new CustomException(ErrorCode.AUTHORIZATION_FAILED)));
    }
}
