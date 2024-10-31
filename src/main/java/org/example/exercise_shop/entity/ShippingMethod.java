package org.example.exercise_shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;

@Entity
@Table(name = "shipping_method")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class ShippingMethod {

    @Id
    @UuidGenerator
    @Column(name = "shipping_method_id", updatable = false, unique = true)
    String id;
    String name;
    String provider;
    Double cost;
    Integer estimatedDays;
    String logo;

    @JsonIgnore
    @OneToMany(mappedBy = "shippingMethod", cascade = CascadeType.ALL)
    List<ShopOrder> orders;
}
