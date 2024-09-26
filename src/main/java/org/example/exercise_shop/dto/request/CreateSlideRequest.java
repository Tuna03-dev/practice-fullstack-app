package org.example.exercise_shop.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CreateSlideRequest {
    String imageUrl;

    String title;

    @Column(length = 500)
    String description;

    String link;

    int orderIndex;

    boolean active;
    String sliderId;


}
