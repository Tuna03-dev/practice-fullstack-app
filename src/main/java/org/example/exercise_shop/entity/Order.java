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
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Order {

    @Id
    @UuidGenerator
    @Column(name = "order_id")
    String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    Address address;


    @Embedded
    @Builder.Default
    Audit audit = new Audit();

    @Column(name = "total_amount")
    BigDecimal totalAmount;
    BigDecimal totalAmountPaid;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    StatusOrder status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_method_id")
    ShippingMethod shippingMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    Set<ShopOrder> shopOrders = new HashSet<>();
}
