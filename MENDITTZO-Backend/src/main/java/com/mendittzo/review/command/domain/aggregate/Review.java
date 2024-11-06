package com.mendittzo.review.command.domain.aggregate;

import com.mendittzo.report.command.domain.aggregate.Report;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE review SET status = 'DELETED', delete_datetime = NOW() WHERE review_id = ? AND status != 'DELETED'")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    @Column(nullable = false)
    private Long bookId;
    @Column(nullable = false)
    private Long userId;
    @CreatedDate
    private LocalDateTime createDateTime;
    @LastModifiedDate
    private LocalDateTime updateDateTime;
    private LocalDateTime deleteDateTime;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private int rating;
    @Enumerated(value = EnumType.STRING)
    private ReviewStatus status = ReviewStatus.CREATED;

    @OneToMany(mappedBy = "review")
    private List<Report> reportList;

    private Review(Long bookId, Long userId, String title, String content, int rating) {
        this.bookId = bookId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.rating = rating;
    }

    public static Review create(Long bookId, Long userId, String title, String content, int rating) {
        return new Review(bookId, userId, title, content, rating);
    }

    public void updateReviewDetails(String title, String content, int rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.status = ReviewStatus.UPDATED;
    }
}
