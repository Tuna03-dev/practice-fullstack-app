package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CartResponse {
    String shopId;
    String shopName;
    List<CartItemResponse> cartItemResponses;
}
