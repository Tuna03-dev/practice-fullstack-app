package org.example.exercise_shop.mapper;


import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.entity.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
