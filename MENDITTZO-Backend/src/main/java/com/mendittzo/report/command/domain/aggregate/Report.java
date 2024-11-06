package com.mendittzo.report.command.domain.aggregate;

import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.user.command.domain.aggregate.User;
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

    // 신고한 유저와의 연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "reporter_user_id", nullable = false)
    private User reporterUser; // 신고한 유저

    // 신고당한 유저와의 연관관계 매핑
    @ManyToOne
    @JoinColumn(name = "reported_user_id", nullable = false)
    private User reportedUser; // 신고당한 유저

    @Column(nullable = true)
    private Long chatroomId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = true)
    private Review review;

    @Column(nullable = true)
    private Long chatId;

    @CreatedDate
    private LocalDateTime createDatetime;

    @Builder
    public Report(Long chatroomId, Review review, User reporterUser, User reportedUser, Long chatId) {
        this.chatroomId = chatroomId;
        this.review = review;
        this.reporterUser = reporterUser;
        this.reportedUser = reportedUser;
        this.chatId = chatId;
    }

}
