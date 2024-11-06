package com.mendittzo.report.command.application.service;

import com.mendittzo.common.exception.CustomException;
import com.mendittzo.common.exception.ErrorCode;
import com.mendittzo.report.command.mapper.ReportMapper;
import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.command.domain.repository.ReviewRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReportCommandService {

    private final ReportCommandRepository reportCommandRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public void requestReport(ReportRequestDTO reportRequestDTO) {

        User reporterUser = userRepository.findByLoginId(reportRequestDTO.getReporterUserId());
        User reportedUser = userRepository.findByLoginId(reportRequestDTO.getReportedUserId());

        switch (reportRequestDTO.getReportType()){
            case REVIEW: reportReview(reportRequestDTO, reporterUser, reportedUser); break;
        }

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

}
