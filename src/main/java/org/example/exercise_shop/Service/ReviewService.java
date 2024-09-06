package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewReponse;
import org.example.exercise_shop.entity.Review;
import org.springframework.data.domain.Page;

public interface ReviewService {
    Page<ReviewReponse> getReviewByProduct(String productId, int page, int size);
    Review addReviewForProduct(AddReviewRequest addReviewRequest);
}
