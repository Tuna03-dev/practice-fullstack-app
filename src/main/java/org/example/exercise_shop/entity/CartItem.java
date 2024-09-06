package org.example.exercise_shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CartItem {
    @Id
    @UuidGenerator
    @Column(name = "cart_item_id",unique = true, updatable = false)
    String id;
    int quantity;
    @Column(name = "price_per_product")
    BigDecimal pricePerProduct;
    @Column(name = "cart_item_amount")
    @Builder.Default
    BigDecimal cartItemAmount =BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    Product product;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @LastModifiedDate
    LocalDateTime reserveAt;

}
