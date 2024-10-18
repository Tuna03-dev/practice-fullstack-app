package org.example.exercise_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.Store;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Discount {

    @Id
    @UuidGenerator
    @Column(name = "discount_id", unique = true, updatable = false)
    String id;
    String name;
    @Enumerated(EnumType.STRING)
    DiscountType type;
    Double percentage;

    BigDecimal amount;

    LocalDateTime startDate;
    LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    DiscountStatus status;

    @ManyToMany(mappedBy = "discounts", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Product> products;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    Shop shop;
}
