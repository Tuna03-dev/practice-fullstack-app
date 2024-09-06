package org.example.exercise_shop.Service;

import org.example.exercise_shop.entity.ShopFee;

import java.time.LocalDateTime;

public interface ShopFeeService {
    ShopFee addShopFee(ShopFee shopFee);
    ShopFee getCurrentShopFee();
}
