package org.example.exercise_shop.dto.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.OrderItem;
import org.example.exercise_shop.entity.StatusOrder;
import org.example.exercise_shop.entity.User;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {

    String receiverName;
    String receiverPhone;
    String receiverAddress;
    String addressId;
    String shippingMethodId;
    int timeDelivery;
    BigDecimal totalAmount;
    BigDecimal totalAmountPaid;
    List<String> cartItemIds;
}
