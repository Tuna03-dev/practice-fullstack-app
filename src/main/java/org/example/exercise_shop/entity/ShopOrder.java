package org.example.exercise_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopOrder {

    @Id
    @UuidGenerator
    @Column(name = "shop_order_id")
    String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    Shop shop;
    LocalDateTime estimatedDeliveryTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shipping_method_id")
    ShippingMethod shippingMethod;

    @Column(name = "total_amount")
    BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    ShopOrderStatus status;

    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL)
    Set<OrderItem> orderItems = new HashSet<>();
    @Embedded
    Audit audit = new Audit();
}
