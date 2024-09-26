package org.example.exercise_shop.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class Product {
    @Id
    @UuidGenerator
    @Column(name = "product_id", updatable = false, unique = true)
    String id;
    @Column(name = "product_name", unique = true)

    String name;
    @Column(name = "stock_quantity")
    int stockQuantity;
    @Column(name = "sold_quantity")
    int soldQuantity;
    @Column(name = "product_price")
    BigDecimal price;
    @Column(name = "average_Rate")
    double averageRate;

    @Column(name = "product_image", length = 512)
    String image;


    @ManyToOne()
    @JoinColumn(name = "category_id")
    @JsonManagedReference
    Category category;

    @ManyToOne()
    @JoinColumn(name = "shop_id")
    @JsonManagedReference
    Shop shop;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    Set<OrderItem> orderItems = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    Set<Review> reviews = new HashSet<>();
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    Set<CartItem> cartItems = new HashSet<>();
    @Embedded
    Audit audit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deleteAt;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "product_discount",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id"))
    private List<Discount> discounts;
}
