package org.example.exercise_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Entity
@Table(name = "reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class Report {
    @Id
    @UuidGenerator
    @Column(name = "report_id", unique = true, updatable = false)
    String id;

    @Column(name = "year")
    int year;
    @Column(name = "month")
    int month;
    @Column(name = "total_orders")
    int totalOrders;
    @Column(name = "total_revenue")
    BigDecimal totalRevenue;
    @Column(name = "profit")
    BigDecimal profit;

    String shopId;

    @Embedded
    @Builder.Default
    Audit audit = new Audit();

}
