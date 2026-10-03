package com.globallearning.lms.catalog.web;

import com.globallearning.lms.catalog.service.ReviewService;
import com.globallearning.lms.catalog.web.dto.ReviewCreateRequest;
import com.globallearning.lms.catalog.web.dto.ReviewResponse;
import com.globallearning.lms.catalog.web.dto.ReviewUpdateRequest;
import com.globallearning.lms.catalog.web.mapper.ReviewMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/courses/{code}/reviews")
public class CourseReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    public CourseReviewController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(
        @PathVariable("code") String code,
        @Valid @RequestBody ReviewCreateRequest request
    ) {
        var review = reviewService.create(
            code,
            request.getLearnerRef(),
            request.getRating(),
            request.getComment()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewMapper.toResponse(review));
    }

    @GetMapping
    public List<ReviewResponse> list(@PathVariable("code") String code) {
        return reviewService.listForCourse(code).stream()
            .map(reviewMapper::toResponse)
            .toList();
    }

    @GetMapping("/{learnerRef}")
    public ReviewResponse getByLearner(
        @PathVariable("code") String code,
        @PathVariable("learnerRef") String learnerRef
    ) {
        var review = reviewService.getForCourseByLearner(code, learnerRef);
        return reviewMapper.toResponse(review);
    }

    @PutMapping("/{learnerRef}")
    public ReviewResponse update(
        @PathVariable("code") String code,
        @PathVariable("learnerRef") String learnerRef,
        @Valid @RequestBody ReviewUpdateRequest request
    ) {
        var review = reviewService.update(
            code,
            learnerRef,
            request.getRating(),
            request.getComment()
        );
        return reviewMapper.toResponse(review);
    }

    @DeleteMapping("/{learnerRef}")
    public ResponseEntity<Void> delete(
        @PathVariable("code") String code,
        @PathVariable("learnerRef") String learnerRef
    ) {
        reviewService.delete(code, learnerRef);
        return ResponseEntity.noContent().build();
    }
}
