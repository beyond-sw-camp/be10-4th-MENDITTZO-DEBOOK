package com.mendittzo.report.command.domain.aggregate;

import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reportId;

    @Column(nullable = true)
    private Long chatroomId;

    @Column(nullable = true)
    private Long reviewId;

    @Column(nullable = true)
    private Long userId;

    @Column(nullable = true)
    private Long chatId;

    @CreatedDate
    private LocalDateTime createDatetime;

    @Builder
    public Report(Long chatroomId, Long reviewId, Long userId, Long chatId) {
        this.chatroomId = chatroomId;
        this.reviewId = reviewId;
        this.userId = userId;
        this.chatId = chatId;
    }

}
