package com.mendittzo.restriction.mapper;

import com.mendittzo.restriction.domain.aggregate.Restriction;
import com.mendittzo.user.command.domain.aggregate.User;

import java.time.LocalDateTime;

public class RestrictionMapper {

    public Restriction toEntity(User user, LocalDateTime endDate) {
        return Restriction.builder()
                .user(user)
                .end_datetime(endDate)
                .build();
    }
}
