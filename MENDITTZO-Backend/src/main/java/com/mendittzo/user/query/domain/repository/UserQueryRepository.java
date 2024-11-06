package com.mendittzo.user.query.domain.repository;

import com.mendittzo.user.command.domain.aggregate.User;

import java.util.Optional;

public interface UserQueryRepository {

    Optional<User> findById(Long userId);
}
