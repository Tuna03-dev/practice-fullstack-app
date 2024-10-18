package org.example.exercise_shop.dto.request;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Category;
import org.example.exercise_shop.entity.StatusShop;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopCreationRequest {

    String name;
    String address;
    Set<Category> categories;
}
