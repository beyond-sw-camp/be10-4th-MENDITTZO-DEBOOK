package com.mendittzo.user.query.application.controller;

import com.mendittzo.security.util.JwtUtil;
import com.mendittzo.user.query.application.dto.UserQueryResponseDTO;
import com.mendittzo.user.query.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user/query") // cqrs구조에 따른 command/query 구분을 위한 엔드포인트 설정
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;
    private final JwtUtil jwtUtil;

    @GetMapping("/info")
    public ResponseEntity<UserQueryResponseDTO> findUserInfo(@RequestHeader("Authorization") String accessToken) {

        // 액세스 토큰에서 userId 추출
        Long loginId = jwtUtil.getLoginId(accessToken);

        UserQueryResponseDTO findUserInfo = userQueryService.findUserInfoByLoginId(loginId);

        return ResponseEntity.ok(findUserInfo);
    }
}
