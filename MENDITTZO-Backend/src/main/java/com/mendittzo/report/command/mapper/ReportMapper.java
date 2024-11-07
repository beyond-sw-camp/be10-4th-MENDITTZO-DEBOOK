package com.mendittzo.report.command.mapper;

import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.review.command.domain.aggregate.Review;
import com.mendittzo.user.command.domain.aggregate.User;

public class ReportMapper {

    public static Report reviewToEntity(User reporterUser, User reportedUser, Review review) {

        return Report.builder().
                review(review).
                reporterUser(reporterUser).
                reportedUser(reportedUser).
                build();
    }
}
