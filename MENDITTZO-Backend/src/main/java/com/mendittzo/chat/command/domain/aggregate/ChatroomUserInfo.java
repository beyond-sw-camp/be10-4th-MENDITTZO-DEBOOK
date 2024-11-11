package com.mendittzo.chat.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ChatroomUserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomInfoId;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    private Long userId;

    @CreatedDate
    private LocalDateTime memberJoinDatetime;

    @Enumerated(EnumType.STRING)
    private ChatroomUserInfoStatus chatroomUserInfoStatus;

    public ChatroomUserInfo(Chatroom chatroom, Long userId, ChatroomUserInfoStatus status) {
        this.chatroom = chatroom;
        this.userId = userId;
        this.memberJoinDatetime = LocalDateTime.now();
        this.chatroomUserInfoStatus = status;
    }

    public void deactivate() {
        this.chatroomUserInfoStatus = ChatroomUserInfoStatus.INACTIVE;
    }
}
