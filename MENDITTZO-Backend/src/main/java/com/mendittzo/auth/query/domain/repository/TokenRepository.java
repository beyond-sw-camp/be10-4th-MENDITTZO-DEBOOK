package com.mendittzo.auth.query.domain.repository;

import com.mendittzo.auth.query.domain.aggregate.Token;

public interface TokenRepository {
    Token save(Token newToken);
}
