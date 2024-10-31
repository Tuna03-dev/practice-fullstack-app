package org.example.exercise_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Table(name = "shop_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopOrder {

    @Id
    @UuidGenerator
    @Column(name = "shop_order_id")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    Shop shop;
    LocalDateTime estimatedDeliveryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id")
    ShippingMethod shippingMethod;

    @Column(name = "total_amount")
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    ShopOrderStatus status;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL)
    Set<OrderItem> orderItems = new HashSet<>();
}
