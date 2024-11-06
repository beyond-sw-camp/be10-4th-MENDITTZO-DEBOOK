package com.mendittzo.user.query.infrastructure;

import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.query.domain.repository.UserQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserQueryRepository extends UserQueryRepository, JpaRepository<User, Long> {

}
