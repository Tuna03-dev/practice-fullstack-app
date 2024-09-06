package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.OrderRequest;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order orderRequestToOrder(OrderRequest orderRequest);
    OrderResponse toOrderReponse(Order order);
}
