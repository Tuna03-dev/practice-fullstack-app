package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.entity.OrderItem;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> findAllOrderItemsByOrderId(String orderId);

}
