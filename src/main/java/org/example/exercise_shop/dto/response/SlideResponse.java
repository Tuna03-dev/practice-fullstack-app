package org.example.exercise_shop.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SlideResponse {
    String id;
    String imageUrl;
    String title;
    String description;
    String link;
    int orderIndex;
    boolean active;

}
