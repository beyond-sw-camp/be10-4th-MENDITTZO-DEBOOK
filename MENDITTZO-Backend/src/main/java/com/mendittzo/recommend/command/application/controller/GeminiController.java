package com.mendittzo.recommend.command.application.controller;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.recommend.command.application.service.ChatHistoryService;
import com.mendittzo.recommend.command.application.service.GeminiService;
import com.mendittzo.recommend.command.domain.aggregate.ChatHistory;
import com.mendittzo.security.util.UserUtil;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.query.domain.repository.UserQueryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Tag(name = "recommend", description = "도서 추천 챗봇 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommend")
public class GeminiController {

    private final GeminiService geminiService;
    private final ChatHistoryService chatHistoryService;
    private final UserQueryRepository userQueryRepository;

    // 챗봇에게 질문 후 답변 받아오기 후 저장
    @PostMapping("/chat")
    public ResponseEntity<?> gemini(@RequestBody String prompt) {

        Long loginId = UserUtil.getCurrentUserLoginId();

        User currentUser = userQueryRepository.findUserInfoByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        try {
            return ResponseEntity.ok().body(geminiService.getContents(prompt, currentUser.getUserId()));
        } catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body("오류가 발생했습니다.: " + e.getMessage());
        }
    }

    // 챗봇과의 채팅 내역 조회
    @GetMapping("/chatlog")
    // userId를 기준으로 모든 채팅내역을 가져옴
    public ResponseEntity<?> getChatLog() {
        Long loginId = UserUtil.getCurrentUserLoginId();

        User currentUser = userQueryRepository.findUserInfoByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        try{

            List<ChatHistory> chatLog = chatHistoryService.getChatHistory(currentUser.getUserId());
            return ResponseEntity.ok().body(chatLog);
        }catch (HttpClientErrorException e) {
            return ResponseEntity.badRequest().body("오류가 발생했습니다.: " + e.getMessage());
        }
    }
}
