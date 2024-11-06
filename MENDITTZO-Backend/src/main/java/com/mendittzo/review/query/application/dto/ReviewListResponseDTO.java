package com.mendittzo.review.query.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewListResponseDTO {

    private List<ReviewResponseDTO> reviewList;  // 리뷰 목록
    private int totalPages;  // 전체 페이지 수
    private long totalElements;  // 전체 리뷰 수
    private int currentPage;  // 현재 페이지 (1-based)
    private int pageSize;  // 한 페이지에 리뷰 수
}
