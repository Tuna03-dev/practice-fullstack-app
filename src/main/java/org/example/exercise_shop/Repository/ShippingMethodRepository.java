package org.example.exercise_shop.Repository;


import org.example.exercise_shop.entity.ShippingMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, String> {
}
