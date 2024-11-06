package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Address;
import org.example.exercise_shop.entity.Audit;
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
    String orderId;
    ShippingMethod shippingMethod;
    Address address;
    String username;
    BigDecimal totalAmount;
    ShopOrderStatus status;
    ShopInformationResponse shopInformationResponse;
    LocalDateTime estimatedDeliveryTime;
    Set<OrderItemResponse> orderItems;
    Audit audit;
}
