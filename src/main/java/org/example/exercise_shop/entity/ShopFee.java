package org.example.exercise_shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shop_fees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopFee {

    @Id
    @UuidGenerator
    @Column(name = "shop_fee_id", updatable = false, unique = true)
    String id;

    double percentage;
    @Column(name = "effective_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate effectiveDate;
}
