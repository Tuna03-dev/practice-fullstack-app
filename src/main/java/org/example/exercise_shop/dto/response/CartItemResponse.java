package org.example.exercise_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Cart;
import org.example.exercise_shop.entity.Product;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CartItemResponse {

    String id;
    int quantity;
    BigDecimal pricePerProduct;
    BigDecimal cartItemAmount;
    String productId;
    String productName;
    String productImage;

}
