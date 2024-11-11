package com.mendittzo.recommend.command.application.service;

import com.mendittzo.recommend.command.domain.aggregate.ChatHistory;
import com.mendittzo.recommend.command.domain.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Autowired
    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    // 새로 입력된 사용자의 요청과 챗봇의 응답을 저장
    public void saveChat(String questionContent, String responseContent, Long userId){
        ChatHistory chatHistory = new ChatHistory(questionContent, responseContent, LocalDateTime.now(), userId);
        chatHistoryRepository.save(chatHistory);
    }

    // 기존에 DB에 저장되어 있던 챗봇과의 대화 내역을 userId를 통해 불러오기
    public List<ChatHistory> getChatHistory(Long userId){
        return chatHistoryRepository.findByUserId(userId);
    }
}
