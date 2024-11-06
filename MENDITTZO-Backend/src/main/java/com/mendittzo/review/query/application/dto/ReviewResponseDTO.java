package com.mendittzo.review.query.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewResponseDTO {

    private String title;
    private String content;
    private int rating;
    private String nickname;
    private LocalDateTime createDatetime;

}
