package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartRequest {
    String productId;
    int quantity;
    BigDecimal pricePerProduct;


}
