package org.example.exercise_shop.mapper;


import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewReponse;
import org.example.exercise_shop.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(AddReviewRequest addReviewRequest);
    ReviewReponse toReviewResponse(Review review);
}
