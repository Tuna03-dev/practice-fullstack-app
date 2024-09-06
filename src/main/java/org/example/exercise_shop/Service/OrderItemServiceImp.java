package org.example.exercise_shop.Service;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.OrderItemRepository;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.entity.OrderItem;
import org.example.exercise_shop.mapper.OrderItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImp implements OrderItemService{

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;


    @Override
    public List<OrderItemResponse> findAllOrderItemsByOrderId(String orderId) {
        return orderItemRepository.findAllByOrderId(orderId).stream().map(orderItem -> {
            OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
            orderItemResponse.setOrderId(orderItem.getOrder().getId());
            orderItemResponse.setProductId(orderItem.getProduct().getId());
            return orderItemResponse;

        }).toList();
    }
}
