package org.example.exercise_shop.dto.response;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {

    String id;
    Audit audit;
    Address address;
    BigDecimal totalAmount;
    BigDecimal totalAmountPaid;
    StatusOrder status;
    Set<ShopOrderResponse> shopOrderResponses;

}
