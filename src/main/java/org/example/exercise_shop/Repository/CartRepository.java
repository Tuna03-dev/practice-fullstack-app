package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUserId(String userId);


}
