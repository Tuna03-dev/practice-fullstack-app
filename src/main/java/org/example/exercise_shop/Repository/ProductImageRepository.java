package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, String> {
    List<ProductImage> findByProductId(String productId);
}
