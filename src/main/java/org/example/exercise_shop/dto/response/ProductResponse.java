package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    BigDecimal price;
    BigDecimal priceWithDiscount;
    String categoryName;
    int soldQuantity;
    int stockQuantity;
    double averageRate;
    String image;
    String description;
    String material;
    String brand;
    String origin;
    String weight;
    String imageDescription;
    String categoryId;
}
