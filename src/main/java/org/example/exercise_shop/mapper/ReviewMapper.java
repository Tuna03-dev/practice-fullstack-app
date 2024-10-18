package org.example.exercise_shop.mapper;


import org.example.exercise_shop.dto.request.AddReviewRequest;
import org.example.exercise_shop.dto.response.ReviewResponse;
import org.example.exercise_shop.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(AddReviewRequest addReviewRequest);
    ReviewResponse toReviewResponse(Review review);
}
