package com.mendittzo.recommend.command.domain.aggregate;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Document(collection = "chathistory")
@Getter
public class ChatHistory {

    @Id
    private String chatId; // MongoDB에서 자동 생성

    @Field("question_content")
    private final String questionContent;

    @Field("response_content")
    private final String responseContent;

    @Field("question_datetime")
    private final LocalDateTime questionDatetime;

    @Field("user_id")
    private final Long userid;

    public ChatHistory(String questionContent, String responseContent, LocalDateTime questionDatetime, Long userid) {
        this.questionContent = questionContent;
        this.responseContent = responseContent;
        this.questionDatetime = questionDatetime;
        this.userid = userid;
    }
}
