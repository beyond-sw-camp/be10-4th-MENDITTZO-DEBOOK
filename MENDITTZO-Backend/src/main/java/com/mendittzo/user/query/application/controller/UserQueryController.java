package com.mendittzo.user.query.application.controller;

import com.mendittzo.user.query.application.dto.UserQueryResponseDTO;
import com.mendittzo.user.query.application.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user/query") // cqrs구조에 따른 command/query 구분을 위한 엔드포인트 설정
@RequiredArgsConstructor
public class UserQueryController {

    private final UserQueryService userQueryService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserQueryResponseDTO> findUserInfo(@PathVariable(name = "userId") Long userId){

        UserQueryResponseDTO findUserInfo = userQueryService.findUserInfo(userId);

        return ResponseEntity.ok(findUserInfo);
    }
}
