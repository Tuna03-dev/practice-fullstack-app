package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, String> {
}
