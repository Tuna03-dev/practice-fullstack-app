package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.CreateDiscountRequest;
import org.example.exercise_shop.entity.Discount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DiscountMapper {
    Discount toDiscount(CreateDiscountRequest createDiscountRequest);
}
