package org.example.exercise_shop.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.Category;
import org.example.exercise_shop.entity.Shop;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductUpdateRequest {

    String id;
    @NotBlank(message = "Product name is required")
    String name;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    int sockQuantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price;

    String categoryId;

}
