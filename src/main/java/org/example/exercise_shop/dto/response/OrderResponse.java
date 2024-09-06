package org.example.exercise_shop.dto.response;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.exercise_shop.entity.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    String id;

    String receiverName;
    String receiverPhone;
    String receiverAddress;
    Audit audit;
    BigDecimal totalAmount;
    StatusOrder status;
    String userId;
    String shopId;


}
