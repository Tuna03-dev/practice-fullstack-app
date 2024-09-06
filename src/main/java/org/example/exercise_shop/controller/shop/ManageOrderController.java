package org.example.exercise_shop.controller.shop;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderItemService;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/orders")
public class ManageOrderController {

    private final ShopService shopService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;

    @GetMapping()
    public ApiResponse<Page<OrderResponse>> getAllOrderByShopId(@RequestParam(value = "page", defaultValue = "0")int page,
                                                                @RequestParam(value = "size", defaultValue = "10")int size){

        Shop shop = shopService.findByCurrentUser();
        Page<OrderResponse> orderResponses = orderService.findAllByShopId(shop.getId(), page, size);

        return ApiResponse.<Page<OrderResponse>>builder()
                .data(orderResponses)
                .build();
    }

    @GetMapping("/{id}/details")
    public ApiResponse<List<OrderItemResponse>> getOrderDetailsByOrderId(@PathVariable(value = "id")String orderId){

        List<OrderItemResponse> orderItemResponses = orderItemService.findAllOrderItemsByOrderId(orderId);
        return ApiResponse.<List<OrderItemResponse>>builder()
                .data(orderItemResponses)
                .build();
    }
}






