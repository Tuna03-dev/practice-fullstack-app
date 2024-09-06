package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartitemRepository extends JpaRepository<CartItem, String> {

    Optional<CartItem> findByProductId(String productId);
    List<CartItem> findAllByCartId(String cartId);
}
