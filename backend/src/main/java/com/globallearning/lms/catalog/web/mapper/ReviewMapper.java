package com.globallearning.lms.catalog.web.mapper;

import com.globallearning.lms.catalog.domain.Review;
import com.globallearning.lms.catalog.web.dto.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public ReviewResponse toResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setCourseCode(review.getCourse().getCode());
        response.setLearnerRef(review.getLearner().getLearnerRef());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        response.setUpdatedAt(review.getUpdatedAt());
        return response;
    }
}
