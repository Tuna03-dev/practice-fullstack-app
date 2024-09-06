package org.example.exercise_shop.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class OrderItemResponse {
    String id;
    int quantity;
    BigDecimal pricePerUnit;
    BigDecimal totalAmount;
    String orderId;
    String productId;

}
