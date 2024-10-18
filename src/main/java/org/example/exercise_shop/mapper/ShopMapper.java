package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.response.ShopInformationResponse;
import org.example.exercise_shop.entity.Shop;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopMapper {
    ShopInformationResponse toShopInformationResponse(Shop shop);
}
