package org.example.exercise_shop.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.StatusShop;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopInformationResponse {
    String id;
    String name;
    String address;
    double averageRate;
    int numberOfRates;
    int numberOfProducts;
    StatusShop status;
    LocalDateTime requestDate;
    double feePercentage;
    String userId;
    String imageUrl;
    String joinedDate;
    String description;
    String descriptionImage;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
