package com.mendittzo.report.command.application.dto;
import com.mendittzo.report.command.domain.aggregate.ReportType;
import jakarta.annotation.Nullable;
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


}
