package org.example.exercise_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportResponse {
    int totalOrders;
    BigDecimal totalRevenue;
    BigDecimal totalProfit;
    String shopId;

}
