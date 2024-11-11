package com.mendittzo.chat.command.application.controller;

import com.mendittzo.chat.command.application.dto.ChatDTO;
import com.mendittzo.chat.command.application.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send")
    public void sendMessage(ChatDTO chatDTO) {
        chatService.saveChat(chatDTO);

        // 특정 채팅방에 메시지 전송
        String destination = "/topic/chatroom/" + chatDTO.getChatroomId();
        messagingTemplate.convertAndSend(destination, chatDTO);
    }

    @GetMapping("/{chatroomId}")
    public ResponseEntity<List<ChatDTO>> getChatsByChatroomId(@PathVariable Long chatroomId) {
        List<ChatDTO> chats = chatService.getChatsByChatroomId(chatroomId);
        return ResponseEntity.ok(chats);
    }
}
