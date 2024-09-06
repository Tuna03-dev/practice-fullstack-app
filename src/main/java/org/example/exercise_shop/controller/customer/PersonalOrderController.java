package org.example.exercise_shop.controller.customer;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderItemService;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.entity.Order;
import org.example.exercise_shop.entity.OrderItem;
import org.example.exercise_shop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/order-histories")
public class PersonalOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping
    public ApiResponse<Page<OrderResponse>> getOrderHistories(@RequestParam(value = "page", defaultValue = "0")int page,
                                                              @RequestParam(value = "size", defaultValue = "10")int size) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Page<OrderResponse> orders = orderService.findAllByUserId(user.getId(), page, size);

        return ApiResponse.<Page<OrderResponse>>builder()
                .data(orders)
                .build();
    }

    @GetMapping("/{id}/details")
    public ApiResponse<List<OrderItemResponse>> getOrderDetailsByOrderId(@PathVariable(value = "id")String orderId){

        List<OrderItemResponse> orderItemResponses = orderItemService.findAllOrderItemsByOrderId(orderId);
        return ApiResponse.<List<OrderItemResponse>>builder()
                .data(orderItemResponses)
                .build();
    }


    @PutMapping("/{id}/cancel")
    public ApiResponse<?> cancelOrder(@PathVariable(value = "id")String orderId){

        orderService.cancelOrder(orderId);
        return ApiResponse.builder()
                .message("Cancelled successfully")
                .build();
    }

}
