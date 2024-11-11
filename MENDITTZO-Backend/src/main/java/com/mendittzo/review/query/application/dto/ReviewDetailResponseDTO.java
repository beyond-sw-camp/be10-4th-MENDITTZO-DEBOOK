package com.mendittzo.review.query.application.dto;

import lombok.Getter;

@Getter
public class ReviewDetailResponseDTO {

    private ReviewResponseDTO review;

    public ReviewDetailResponseDTO( ReviewResponseDTO review) {

        this.review = review;
    }
}
