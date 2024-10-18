package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String id;
    int rating;
    String comment;
    String material;
    String trueDescription;
    String colour;
    String createdAt;
    String updatedAt;
    String productId;
    String userId;
    String userName;
    String userAvatar;
    int likes;
}
