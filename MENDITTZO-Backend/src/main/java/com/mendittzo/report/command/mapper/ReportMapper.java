package com.mendittzo.report.command.mapper;

import com.mendittzo.chat.command.domain.aggregate.Chat;
import com.mendittzo.chat.command.domain.aggregate.Chatroom;
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

    public static Report chatroomToEntity(User reporterUser, User reportedUser, Chatroom chatroom) {

        return Report.builder().
                chatroomId(chatroom.getChatroomId()).
                reporterUser(reporterUser).
                reportedUser(reportedUser).
                build();
    }

    public static Report chatToEntity(User reporterUser, User reportedUser, Chat reportChat) {

        return Report.builder()
                .chatId(reportChat.getChatId())
                .reporterUser(reporterUser)
                .reportedUser(reportedUser)
                .build();
    }
}
