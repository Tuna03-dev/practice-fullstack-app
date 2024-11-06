package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.ShopOrderRepository;
import org.example.exercise_shop.entity.ShopOrderStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShopOrderServiceImp implements ShopOrderService {
   private final ShopOrderRepository shopOrderRepository;

    @Override
    public int countByShop_IdAndStatus(String shopId, ShopOrderStatus status) {
        return shopOrderRepository.countByShop_IdAndStatus(shopId, status);
    }
}
