package com.mendittzo.chat.command.application.dto;

import com.mendittzo.chat.command.domain.aggregate.Chatroom;
import com.mendittzo.chat.command.domain.aggregate.ChatroomStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ChatroomDTO {

    private Long chatroomId;
    private Long bookId;
    private LocalDateTime openDatetime;
    private Long maxMemberCount;
    private Long currentMemberCount;
    private String title;
    private ChatroomStatus status;

    public ChatroomDTO(Chatroom chatroom) {
        this.chatroomId = chatroom.getChatroomId();
        this.bookId = chatroom.getBookId();
        this.openDatetime = chatroom.getOpenDatetime();
        this.maxMemberCount = chatroom.getMaxMemberCount();
        this.title = chatroom.getTitle();
        this.status = chatroom.getStatus();
    }

    public ChatroomDTO(Chatroom chatroom, long currentMemberCount) {
        this.chatroomId = chatroom.getChatroomId();
        this.bookId = chatroom.getBookId();
        this.openDatetime = chatroom.getOpenDatetime();
        this.maxMemberCount = chatroom.getMaxMemberCount();
        this.currentMemberCount = currentMemberCount;
        this.title = chatroom.getTitle();
        this.status = chatroom.getStatus();
    }

}
