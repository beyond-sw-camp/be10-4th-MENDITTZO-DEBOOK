package com.mendittzo.review.query.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewListResponseDTO {

    private List<ReviewResponseDTO> reviewList;

}
