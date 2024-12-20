package org.example.exercise_shop.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.exercise_shop.entity.*;


import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductCreationRequest {
    @NotBlank(message = "Product name is required")
    String name;
    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    int stockQuantity;
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    BigDecimal price;
    @NotNull(message = "Category is required")
    String categoryId;
    String image;
    String description;
    String material;
    String brand;
    String origin;
    String weight;
    String imageDescription;
    List<String> listImages;


}
