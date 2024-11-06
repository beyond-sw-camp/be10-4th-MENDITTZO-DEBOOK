package com.mendittzo.report.command.application.service;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.report.command.mapper.ReportMapper;
import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import com.mendittzo.restriction.domain.aggregate.Restriction;
import com.mendittzo.restriction.domain.repository.RestrictionRepository;
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
    private final UserRepository userRepository;

    private final RestrictionRepository restrictionRepository;

    @Transactional
    public void requestReport(ReportRequestDTO reportRequestDTO) {

        User reporterUser = userRepository.findByLoginId(reportRequestDTO.getReporterUserId());
        User reportedUser = userRepository.findByLoginId(reportRequestDTO.getReportedUserId());

        switch (reportRequestDTO.getReportType()){
            case REVIEW: reportReview(reportRequestDTO, reporterUser, reportedUser); break;
        }

        checkAndRestrictUser(reportedUser);

    }

    private void reportReview(ReportRequestDTO reportRequestDTO, User reporterUser, User reportedUser) {

        Review reportedReview = reviewRepository.findById(reportRequestDTO.getReviewId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_REVIEW)
        );

        System.out.println(reportedReview.getReportList().size());

        Report newReport = ReportMapper.reviewToEntity(reporterUser, reportedUser, reportedReview);

        reportCommandRepository.save(newReport);

        if(reportedReview.getReportList().size() +1 == 10 ){
            reviewRepository.deleteById(reportRequestDTO.getReviewId());
        }
    }

    private void checkAndRestrictUser(User user) {

        int reportSize = user.getReportedUser().size() + 1;

        if(reportSize == 10){
            user.restrictUser(LocalDateTime.now().plusDays(10));
        } else if (reportSize == 20) {
            user.restrictUser(LocalDateTime.now().plusDays(10));
        } else if (reportSize == 30) {
            // 임시로 100년 정지
            user.restrictUser(LocalDateTime.now().plusYears(100));
        }
    }

    // 매일 자정에 제재 확인 후 해제
    @Scheduled(cron = "0 0 0 * * *")
    public void restrictionLifted(){
        List<Restriction> restrictions = restrictionRepository.findAllByEndDateTimeBeforeAnd
    }
}
