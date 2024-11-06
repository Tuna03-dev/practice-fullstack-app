package org.example.exercise_shop.controller.customer;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderItemService;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.dto.response.ShopOrderResponse;
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
    public ApiResponse<Page<ShopOrderResponse>> getOrderHistories(@RequestParam(value = "page", defaultValue = "0")int page,
                                                                  @RequestParam(value = "size", defaultValue = "3")int size,
                                                                  @RequestParam(value = "type", defaultValue = "all")String type,
                                                                  @RequestParam(value = "search", defaultValue = "")String search){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Page<ShopOrderResponse> orders = orderService.findAllByUserId(user.getId(), page, size, type, search);

        return ApiResponse.<Page<ShopOrderResponse>>builder()
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
    public ApiResponse<?> cancelOrder(@PathVariable(value = "id")String shopOrderId){

        orderService.cancelOrder(shopOrderId);
        return ApiResponse.builder()
                .message("Cancelled successfully")
                .build();
    }

}
