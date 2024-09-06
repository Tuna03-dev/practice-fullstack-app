package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findAllByOrderId(String OrderId);
}
