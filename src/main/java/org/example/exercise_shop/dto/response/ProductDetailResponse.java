package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDetailResponse {
    String id;
    String name;
    BigDecimal price;
    BigDecimal priceWithDiscount;
    String categoryName;
    int soldQuantity;
    int stockQuantity;
    double averageRate;
    String image;
    List<ProductImageResponse> productImages;
    String shopId;
}