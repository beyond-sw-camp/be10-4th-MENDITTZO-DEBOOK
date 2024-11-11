package com.mendittzo.report.command.application.service;

import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.aggregate.ReportType;
import com.mendittzo.restriction.domain.repository.RestrictionHistoryRepository;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.command.domain.repository.ReviewRepository;
import com.mendittzo.user.command.domain.aggregate.Status;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class ReportCommandServiceTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestrictionHistoryRepository restrictionHistoryRepository;

    @Autowired
    private ReportCommandService reportCommandService;

    @Test
    @Transactional
    @DisplayName("report Review 테스트 : 리뷰 신고 테스트")
    void reportReview() {

        // given
        Long reporterLoginId = 123456789L;
        Long reportedLoginId = 987654321L;

        ReportRequestDTO reportRequestDTO = new ReportRequestDTO();
        reportRequestDTO.setReporterUserId(reporterLoginId);
        reportRequestDTO.setReportedUserId(reportedLoginId);
        reportRequestDTO.setReportType(ReportType.REVIEW);
        reportRequestDTO.setReviewId(1L);

        User reporterUser = User.create(
                100L,
                "bookfox@gmail.com",
                "책 먹는 여우",
                Status.ACTIVE,
                "KAKAO",
                "http://example.com/profile.jpg",
                LocalDateTime.now(),
                null,
                reporterLoginId
        );
        User reportedUser = User.create(
                101L,
                "bookCat@gmail.com",
                "책 뱉는 고양이",
                Status.ACTIVE,
                "KAKAO",
                "http://example.com/profile.jpg",
                LocalDateTime.now(),
                null,
                reportedLoginId
        );
        Review review = Review.create(
                116177L,
                101L,
                "testTitle",
                "testContent",
                4
        );

        userRepository.save(reporterUser);
        userRepository.save(reportedUser);
        reviewRepository.save(review);

        // when
        reportCommandService.requestReport(reportRequestDTO);

        // then
        User user = userRepository.findByLoginId(reportedLoginId);
        Report report = user.getReportedUser().get(0);
        assertEquals(report.getReportedUser().getLoginId(), reportedLoginId);
        assertEquals(report.getReporterUser().getLoginId(), reporterLoginId);
    }
}