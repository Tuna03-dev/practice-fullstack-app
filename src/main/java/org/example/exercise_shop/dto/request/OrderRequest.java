package org.example.exercise_shop.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;


import java.math.BigDecimal;

import java.util.List;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {


    String addressId;
    BigDecimal totalAmount;
    BigDecimal totalAmountPaid;
    List<ShopOrderRequest> shopOrderRequests;
}
