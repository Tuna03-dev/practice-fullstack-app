package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewResponse;
import org.example.exercise_shop.entity.Review;
import org.springframework.data.domain.Page;

public interface ReviewService {
    Page<ReviewResponse> getReviewByProduct(String productId, int page, int size, String sort);
    Review addReviewForProduct(AddReviewRequest addReviewRequest);
}
