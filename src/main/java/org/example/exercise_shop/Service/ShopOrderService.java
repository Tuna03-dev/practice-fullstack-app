package org.example.exercise_shop.Service;

import org.example.exercise_shop.entity.ShopOrderStatus;

public interface ShopOrderService {
    int countByShop_IdAndStatus(String shopId, ShopOrderStatus status);
}
