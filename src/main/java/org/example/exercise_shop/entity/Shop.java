package org.example.exercise_shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shops")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Shop {

    @Id
    @UuidGenerator
    @Column(name = "shop_id", unique = true, updatable = false)
    String id;
    @Column(name = "shop_name")
    String name;
    String address;

    @Column(name = "shop_average_rate")
    double averageRate;
    int numberOfRates;
    @Enumerated(EnumType.STRING)
    @Column(name = "shop_status")
    StatusShop status;
    @Column(name = "request_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime requestDate;
    double feePercentage;
    String userId;
    @Column(length = 512)
    String imageUrl;
    @Embedded
    Audit audit;
    @Column(length = 512)
    String description;
    @Column(length = 512)
    String descriptionImage;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "category_shop",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    Set<Category> categories;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    @JsonIgnore
    Set<Product> products;



    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    List<Order> orders;

    @OneToMany(mappedBy = "shop")
    List<Discount> discounts;
}
