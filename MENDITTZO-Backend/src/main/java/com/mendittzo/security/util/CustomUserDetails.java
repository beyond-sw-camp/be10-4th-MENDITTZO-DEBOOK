package com.mendittzo.security.util;

import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//  UserDetails 에서 loginId 를 가져오기 위한 커스텀 UserDetails 클래스

public class CustomUserDetails implements UserDetails {

    // loginId 를 가져오는 메소드
    private final Long loginId;
    private final Long userId;
    private final Status status;
//    private final String username;  // UserDetails에서 사용하는 기본 필드
//    private final String password;  // UserDetails에서 사용하는 기본 필드

    public Long getLoginId() {
        return loginId;
    }

    public Long getUserId() {
        return userId;
    }

    public Status getStatus() {
        return status;
    }

    public CustomUserDetails(Long loginId, Long userId, Status status) {
        this.loginId = loginId;
        this.userId = userId;
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
