package com.mendittzo.chat.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE chatroom SET status = 'CLOSED' WHERE chatroom_id = ? AND status != 'DELETED'")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatroomId;

    private Long bookId;

    @CreatedDate
    private LocalDateTime openDatetime;

    private Long maxMemberCount;

    private String title;

    @Enumerated(EnumType.STRING)
    private ChatroomStatus status;

    @Builder
    public Chatroom(Long bookId, LocalDateTime openDatetime, Long maxMemberCount, String title, ChatroomStatus status) {
        this.bookId = bookId;
        this.openDatetime = openDatetime;
        this.maxMemberCount = maxMemberCount;
        this.title = title;
        this.status = status;
    }
}
