package com.mendittzo.user.query.application.controller;

import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.security.util.JwtUtil;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.query.application.dto.UserQueryResponseDTO;
import com.mendittzo.user.query.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/user/query") // cqrs구조에 따른 command/query 구분을 위한 엔드포인트 설정
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;
    private final JwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<UserQueryResponseDTO> findUserInfo() {

        // 인증 객체에서 loginId 꺼내기
        Long loginId = UserUtil.getCurrentUserLoginId();

        if (loginId == null) {
            return ResponseEntity.status(401).build();
        }

        UserQueryResponseDTO findUserInfo = userQueryService.findUserInfoByLoginId(loginId);
        log.info("dgdgd",findUserInfo.getUserId());

        return ResponseEntity.ok(findUserInfo);
    }
}
