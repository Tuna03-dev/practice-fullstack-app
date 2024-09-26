package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.DiscountStatus;
import org.example.exercise_shop.entity.DiscountType;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDiscountRequest {

    String name;
    DiscountType type;
    Double percentage;
    BigDecimal amount;
    LocalDateTime startDate;
    LocalDateTime endDate;
    DiscountStatus status;
    List<String> productIds;
    String shopId;
}
