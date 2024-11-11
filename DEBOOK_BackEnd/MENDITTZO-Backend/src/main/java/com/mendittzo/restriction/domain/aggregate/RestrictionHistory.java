package com.mendittzo.restriction.domain.aggregate;

import com.mendittzo.user.command.domain.aggregate.User;
import jakarta.persistence.*;
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
public class RestrictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long restrictionId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User restrictionUser;

    @Column
    @Enumerated(EnumType.STRING)
    RestrictionStatus restrictionStatus;

    @Column
    @CreatedDate
    LocalDateTime createDatetime;

    @Column
    LocalDateTime endDatetime;

    @Builder
    public RestrictionHistory(User user, LocalDateTime end_datetime) {

        this.restrictionUser = user;
        this.restrictionStatus = RestrictionStatus.ACTIVE;
        this.endDatetime = end_datetime;
    }

    public void endRestriction() {
        this.restrictionStatus = RestrictionStatus.END;
    }
}
