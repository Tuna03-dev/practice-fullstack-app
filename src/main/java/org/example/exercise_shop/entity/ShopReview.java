package org.example.exercise_shop.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ShopReview {

    @Id
    @UuidGenerator
    @Column(name = "review_id", updatable = false, unique = true)
    String id;
    int rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    Shop shop;

    @Builder.Default
    @Embedded
    Audit audit = new Audit();

}
