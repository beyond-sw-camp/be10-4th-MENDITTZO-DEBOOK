package com.mendittzo.chat.command.application.controller;

import com.mendittzo.chat.command.application.dto.ChatroomDTO;
import com.mendittzo.chat.command.application.service.ChatroomService;
import com.mendittzo.security.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chatrooms")
@RequiredArgsConstructor
public class ChatroomController {

    private final ChatroomService chatroomService;

    @PostMapping
    public ResponseEntity<ChatroomDTO> createChatRoom(@RequestBody @Valid ChatroomDTO chatRoomDTO) {
        ChatroomDTO createdChatroom = chatroomService.createChatRoom(chatRoomDTO);
        return ResponseEntity.ok(createdChatroom);
    }

    @GetMapping
    public ResponseEntity<List<ChatroomDTO>> getAllChatRooms() {
        List<ChatroomDTO> chatrooms = chatroomService.getAllChatrooms();
        return ResponseEntity.ok(chatrooms);
    }

    @GetMapping("/{chatroomId}")
    public ResponseEntity<ChatroomDTO> getChatRoomById(@PathVariable Long chatroomId) {
        ChatroomDTO chatroom = chatroomService.getChatroomById(chatroomId);
        return ResponseEntity.ok(chatroom);
    }

    @PostMapping("/{chatroomId}/enter")
    public ResponseEntity<ChatroomDTO> enterChatroom(@PathVariable Long chatroomId) {
        Long userId = UserUtil.getCurrentUserLoginId();
        ChatroomDTO chatroom = chatroomService.enterChatroom(chatroomId, userId);
        return ResponseEntity.ok(chatroom);
    }

    @PostMapping("/{chatroomId}/leave")
    public ResponseEntity<ChatroomDTO> leaveChatroom(@PathVariable Long chatroomId) {
        Long userId = UserUtil.getCurrentUserLoginId();
        ChatroomDTO chatroom = chatroomService.leaveChatroom(chatroomId, userId);
        return ResponseEntity.ok(chatroom);
    }
}
