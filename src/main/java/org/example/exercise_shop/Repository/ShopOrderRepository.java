package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.ShopOrder;
import org.example.exercise_shop.entity.ShopOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, String> {

    int countByShop_IdAndStatus(String shopId, ShopOrderStatus status);
}