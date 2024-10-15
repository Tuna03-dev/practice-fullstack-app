package org.example.exercise_shop.controller.customer;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ReviewService;
import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ReviewResponse;
import org.example.exercise_shop.entity.Review;
import org.example.exercise_shop.mapper.ReviewMapper;
import org.example.exercise_shop.utils.DateTimeFormatter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer/rate-products")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;
    private final DateTimeFormatter dateTimeFormatter;

    @PostMapping("/create")
    public ApiResponse<ReviewResponse> createNewReviews(@RequestBody AddReviewRequest addReviewRequest){
        Review review = reviewService.addReviewForProduct(addReviewRequest);

        ReviewResponse reviewResponse = reviewMapper.toReviewResponse(review);
        reviewResponse.setCreatedAt(dateTimeFormatter.format(review.getAudit().getCreatedAt()));
        reviewResponse.setUpdatedAt(dateTimeFormatter.format(review.getAudit().getUpdatedAt()));
        reviewResponse.setProductId(review.getProduct().getId());
        reviewResponse.setUserId(review.getUser().getId());

        return ApiResponse.<ReviewResponse>builder()
                .data(reviewResponse)
                .message("Create review successfully")
                .build();
    }

}
