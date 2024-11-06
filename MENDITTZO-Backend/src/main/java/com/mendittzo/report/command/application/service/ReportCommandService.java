package com.mendittzo.report.command.application.service;

import com.mendittzo.report.command.application.dto.ReportRequestDTO;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.repository.ReportCommandRepository;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.review.command.domain.repository.ReviewRepository;
import com.mendittzo.user.command.domain.aggregate.User;
import com.mendittzo.user.command.domain.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        Report newReport = reportRequestDTO.toEntity(reporterUser, reportedUser);

        reportCommandRepository.save(newReport);
    }

//    private Integer checkReview(Long reviewId) {
//
//        List<Review> reviewList = reviewRepository.
//    }
}
