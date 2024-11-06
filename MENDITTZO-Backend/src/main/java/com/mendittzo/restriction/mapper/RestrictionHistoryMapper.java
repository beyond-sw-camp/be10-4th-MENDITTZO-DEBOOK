package com.mendittzo.restriction.mapper;

import com.mendittzo.restriction.domain.aggregate.RestrictionHistory;
import com.mendittzo.user.command.domain.aggregate.User;

import java.time.LocalDateTime;

public class RestrictionHistoryMapper {

    public RestrictionHistory toEntity(User user, LocalDateTime endDate) {
        return RestrictionHistory.builder()
                .user(user)
                .end_datetime(endDate)
                .build();
    }
}
