package com.mendittzo.auth.query.infrastructure.repository;

import com.mendittzo.auth.query.domain.aggregate.Token;
import com.mendittzo.auth.query.domain.repository.TokenRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTokenRepository extends JpaRepository<Token, Long>, TokenRepository {

}
