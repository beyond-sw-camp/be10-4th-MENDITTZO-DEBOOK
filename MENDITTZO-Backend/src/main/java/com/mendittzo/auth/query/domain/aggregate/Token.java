package com.mendittzo.auth.query.domain.aggregate;

// todo: redis 사용 전 임시로 maraiaDB 에 저장용

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "token")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Token {

    @Id
    @JsonProperty("token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    @JsonProperty("login_id")
    @Column(nullable = false)
    private Long loginId;

    @JsonProperty("access_token")
    @Column(nullable = false)
    private String accessToken;

    @JsonProperty("access_token_expire_datetime")
    @Column(nullable = false)
    private Long accessTokenExpireDatetime;

    @JsonProperty("refresh_token")
    @Column(nullable = false)
    private String refreshToken;

    @JsonProperty("refresh_token_expire_datetime")
    @Column(nullable = false)
    private Long refreshTokenExpireDatetime;

    public Token(Long loginId, String accessToken, Long accessTokenExpireDatetime, String refreshToken, Long refreshTokenExpireDatetime) {
        this.loginId = loginId;
        this.accessToken = accessToken;
        this.accessTokenExpireDatetime = accessTokenExpireDatetime;
        this.refreshToken = refreshToken;
        this.refreshTokenExpireDatetime = refreshTokenExpireDatetime;
    }

    public static Token create(Long loginId, String accessToken, Long accessTokenExpireDatetime, String refreshToken, Long refreshTokenExpireDatetime) {
        return new Token(loginId, accessToken, accessTokenExpireDatetime, refreshToken, refreshTokenExpireDatetime);
    }
}
