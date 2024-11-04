package com.mendittzo.report.command.application.dto;
import com.mendittzo.report.command.domain.aggregate.Report;
import lombok.Data;

@Data
public class ReportRequestDTO {

    private Long chatroomId;

    private Long reviewId;

    private Long chatId;

    private Long userId;

    public Report toEntity() {

        return Report.builder().
                chatroomId(chatroomId).
                reviewId(reviewId).
                userId(userId).
                chatId(chatId).
                build();
    }

}
