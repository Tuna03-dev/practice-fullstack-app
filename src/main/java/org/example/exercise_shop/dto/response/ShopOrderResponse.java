package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.ShippingMethod;
import org.example.exercise_shop.entity.ShopOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopOrderResponse {
    String id;
    ShippingMethod shippingMethod;
    BigDecimal totalAmount;
    ShopOrderStatus status;
    ShopInformationResponse shopInformationResponse;
    LocalDateTime estimatedDeliveryTime;
    Set<OrderItemResponse> orderItems;
}
