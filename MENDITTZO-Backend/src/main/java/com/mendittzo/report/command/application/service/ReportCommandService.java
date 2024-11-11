package com.mendittzo.report.command.application.service;

import com.mendittzo.chat.command.domain.aggregate.Chat;
import com.mendittzo.chat.command.domain.aggregate.Chatroom;
import com.mendittzo.chat.command.domain.repository.ChatRepository;
import com.mendittzo.chat.command.domain.repository.ChatroomRepository;
import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.report.command.mapper.ReportMapper;
import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import com.mendittzo.restriction.domain.aggregate.RestrictionHistory;
import com.mendittzo.restriction.domain.aggregate.RestrictionStatus;
import com.mendittzo.restriction.domain.repository.RestrictionHistoryRepository;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.command.domain.repository.ReviewRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReportCommandService {

    private final ReportCommandRepository reportCommandRepository;
    private final ReviewRepository reviewRepository;
    private final ChatRepository chatRepository;
    private final ChatroomRepository chatroomRepository;
    private final UserRepository userRepository;

    private final RestrictionHistoryRepository restrictionHistoryRepository;

    @Transactional
    public void requestReport(ReportRequestDTO reportRequestDTO) {

        // 신고자, 피신고자 데이터 조회
        User reporterUser = userRepository.findByLoginId(reportRequestDTO.getReporterUserId());
        User reportedUser = userRepository.findByLoginId(reportRequestDTO.getReportedUserId());

        // 신고 타입에 따른 처리
        switch (reportRequestDTO.getReportType()) {
            case REVIEW:
                reportReview(reportRequestDTO, reporterUser, reportedUser);
                break;
            case CHATROOM:
                reportChatroom(reportRequestDTO, reporterUser, reportedUser);
                break;
        }

        // 피신고자 정지 유무 확인 및 처리
        checkAndRestrict(reportedUser);
    }

    private void reportReview(ReportRequestDTO reportRequestDTO, User reporterUser, User reportedUser) {

        Review reportedReview = reviewRepository.findById(reportRequestDTO.getReviewId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_REVIEW)
        );

        Report newReport = ReportMapper.reviewToEntity(reporterUser, reportedUser, reportedReview);

        reportCommandRepository.save(newReport);

        if (reportedReview.getReportList().size() + 1 == 5) { // review의 reportList.size가 최신화 안됌.
            reviewRepository.deleteById(reportRequestDTO.getReviewId());
        }
    }

    private void reportChatroom(ReportRequestDTO reportRequestDTO, User reporterUser, User reportedUser) {

        Chatroom reportChatroom = chatroomRepository.findById(reportRequestDTO.getChatroomId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_CHATROOM)
        );

        Report newReport = ReportMapper.chatroomToEntity(reporterUser, reportedUser, reportChatroom);

        reportCommandRepository.save(newReport);

        List<Report> reportList = reportCommandRepository.findAllByChatroomId(reportChatroom.getChatroomId());

        if(reportList.size() == 10){
            chatroomRepository.deleteById(reportRequestDTO.getChatroomId());
        }
    }

    private void reportChat(ReportRequestDTO reportRequestDTO, User reporterUser, User reportedUser) {

        Chat reportChat = chatRepository.findById(reportRequestDTO.getChatId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_CHAT)
        );

        Report newReport = ReportMapper.chatToEntity(reporterUser, reportedUser, reportChat);

        reportCommandRepository.save(newReport);

        List<Report> reportList = reportCommandRepository.findAllByChatId(reportChat.getChatId());

        if(reportList.size() == 5){
            chatroomRepository.deleteById(reportRequestDTO.getChatroomId());
        }
    }

    private void checkAndRestrict(User user) {

        int reportSize = user.getReportedUser().size();

        // 신고 횟수에 따른 제재 처리
        if (reportSize == 10) {
            restrictionCreate(user, 10);
            user.restrictUser();
        } else if (reportSize == 20) {
            restrictionCreate(user, 10);
            user.restrictUser();
        } else if (reportSize == 30) {
            // 임시로 100일 정지
            restrictionCreate(user, 100);
            user.restrictUser();
        }
    }

    // 제재 생성
    private void restrictionCreate(User user, int days) {

        RestrictionHistory restrictionHistory;

        restrictionHistory = RestrictionHistory.builder()
                .user(user)
                .end_datetime(LocalDateTime.now().plusDays(days))
                .build();

        restrictionHistoryRepository.save(restrictionHistory);
    }

    // 매일 자정에 제재 확인 후 해제
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void allRestrictionLifted() {

        System.out.println("시작");

        // endDatetime이 현재보다 전이고, 제재 상태가 ACTIVE인 제재 내역 조회
        List<RestrictionHistory> restrictionHistories = restrictionHistoryRepository
                .findAllByEndDatetimeBeforeAndRestrictionStatus(LocalDateTime.now(), RestrictionStatus.ACTIVE);

        System.out.println(restrictionHistories.toString());

        System.out.println(restrictionHistories.toString());

        if (!restrictionHistories.isEmpty()) {
            // 모든 제재 내역에 해당하는 유저 제재 해제
            restrictionHistories.forEach(restriction -> {
                User user = userRepository.
                        findByLoginId(restriction.getRestrictionUser().getLoginId());
                user.restrictionLifted();
                restriction.endRestriction();
                userRepository.save(user);
            });
        }
    }
}
