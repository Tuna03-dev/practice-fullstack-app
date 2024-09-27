package org.example.exercise_shop.controller.customer;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ReviewService;
import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ReviewReponse;
import org.example.exercise_shop.entity.Review;
import org.example.exercise_shop.mapper.ReviewMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer/rate-products")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;


    @PostMapping("/create")
    public ApiResponse<ReviewReponse> createNewReviews(@RequestBody AddReviewRequest addReviewRequest){
        Review review = reviewService.addReviewForProduct(addReviewRequest);

        ReviewReponse reviewReponse = reviewMapper.toReviewResponse(review);
        reviewReponse.setCreatedAt(review.getAudit().getCreatedAt());
        reviewReponse.setUpdatedAt(review.getAudit().getUpdatedAt());
        reviewReponse.setProductId(review.getProduct().getId());
        reviewReponse.setUserId(review.getUser().getId());

        return ApiResponse.<ReviewReponse>builder()
                .data(reviewReponse)
                .message("Create review successfully")
                .build();
    }

}
