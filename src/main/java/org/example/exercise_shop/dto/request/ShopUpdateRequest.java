package org.example.exercise_shop.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Category;

import java.util.Set;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShopUpdateRequest {
    String id;
    String name;
    String address;
    String imageUrl;
    String description;
    String descriptionImage;
    Set<Category> categories;
}
