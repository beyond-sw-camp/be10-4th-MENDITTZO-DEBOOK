package com.mendittzo.report.command.application.dto;
import com.mendittzo.report.command.domain.aggregate.Report;
import com.mendittzo.report.command.domain.aggregate.ReportType;
import com.mendittzo.user.command.domain.aggregate.User;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportRequestDTO {

    @Nullable
    private Long chatroomId;

    @Nullable
    private Long reviewId;

    @Nullable
    private Long chatId;

    @Nullable
    private Long reporterUserId; // 신고자 유저 아이디

    @Nullable
    private Long reportedUserId; // 피신고자 유저 아이디


    private ReportType reportType;

    public Report toEntity(User reporterUser, User reportedUser) {

        return Report.builder().
                chatroomId(chatroomId).
                reviewId(reviewId).
                reporterUser(reporterUser).
                reportedUser(reportedUser).
                chatId(chatId).
                build();
    }

}
