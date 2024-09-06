package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Audit;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewReponse {
    String id;
    int rating;
    String comment;
    String productId;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userId;
}
