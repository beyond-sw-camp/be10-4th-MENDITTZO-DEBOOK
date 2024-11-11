package com.mendittzo.chat.command.application.service;

import com.mendittzo.chat.command.application.dto.ChatDTO;
import com.mendittzo.chat.command.domain.aggregate.Chat;
import com.mendittzo.chat.command.domain.repository.ChatRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveChat(ChatDTO chatDTO) {
//        User user = userRepository.findByLoginId(loginId);
//        log.info("여기야 여기 loginId {}", loginId);

        Chat chat = Chat.builder()
                .chatId(UUID.randomUUID().toString())
                .chatContent(chatDTO.getChatContent())
                .createDatetime(LocalDateTime.now())
//                .userId(user.getUserId())
                .chatroomId(chatDTO.getChatroomId())
                .build();

        chatRepository.save(chat);
    }

    @Transactional(readOnly = true)
    public List<ChatDTO> getChatsByChatroomId(Long chatroomId) {
        List<Chat> chats = chatRepository.findByChatroomId(chatroomId);
        return chats.stream()
                .map(chat -> new ChatDTO(chat.getChatContent(), chat.getCreateDatetime(), chat.getChatroomId()))
                .collect(Collectors.toList());
    }
}
