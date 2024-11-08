package com.mendittzo.security.service;

import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class userService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetails loadUserByLoginId(Long loginId) {

        User user = userRepository.findByLoginId(loginId);
        if(user == null) {
            throw new UsernameNotFoundException("해당 loginId로 사용자를 찾을 수 없습니다.");
        }

        return new CustomUserDetails(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("username을 이용한 조회 불가");
    }
}
