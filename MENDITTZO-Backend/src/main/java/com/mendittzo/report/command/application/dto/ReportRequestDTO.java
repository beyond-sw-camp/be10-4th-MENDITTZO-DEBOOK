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
    private String chatId;

    @Nullable
    private Long reporterUserId; // 신고자 유저 아이디 >> 컨트롤러에서 로그인 중인 유저 id 가져올 예정

    @Nullable
    private Long reportedUserId; // 피신고자 유저 아이디

    private ReportType reportType;


}
