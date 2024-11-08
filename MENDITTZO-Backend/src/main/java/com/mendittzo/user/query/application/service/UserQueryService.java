package com.mendittzo.user.query.application.service;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.query.application.dto.UserQueryResponseDTO;
import com.mendittzo.user.query.domain.repository.UserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserQueryRepository userQueryRepository;

//    public UserQueryResponseDTO findUserInfo(Long userId) {
//
//        User user = userQueryRepository.findById(userId)
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
//
//        return user.createQueryResponseDTO();
//    }

    public UserQueryResponseDTO findUserInfoByLoginId(Long loginId) {
        User user = userQueryRepository.findUserInfoByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return user.createQueryResponseDTO();
    }
}
