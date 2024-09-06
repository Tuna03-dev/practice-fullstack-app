package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, String> {
    Page<Review> findAllByProductId(String productId, Pageable pageable);

}
