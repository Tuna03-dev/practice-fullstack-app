package org.example.exercise_shop.controller.shop;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderItemService;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.Service.ShopOrderService;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.dto.response.ShopOrderResponse;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.ShopOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/orders")
public class ManageOrderController {

    private final ShopService shopService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ShopOrderService shopOrderService;

    @GetMapping()
    public ApiResponse<Page<ShopOrderResponse>> getAllOrderByShopId(@RequestParam(value = "page", defaultValue = "0")int page,
                                                                    @RequestParam(value = "size", defaultValue = "10")int size){

        Shop shop = shopService.findByCurrentUser();
        Page<ShopOrderResponse> orderResponses = orderService.findAllByShopId(shop.getId(), page, size);

        return ApiResponse.<Page<ShopOrderResponse>>builder()
                .data(orderResponses)
                .build();
    }

    @GetMapping("/number")
    public ApiResponse<Object> getNumberOrderDetailsByShopId(){

        Shop shop = shopService.findByCurrentUser();
        Map<String, Integer> numberOrderDetails = new HashMap<>();
        numberOrderDetails.put("pending", shopOrderService.countByShop_IdAndStatus(shop.getId(), ShopOrderStatus.PENDING ));
        numberOrderDetails.put("shipping", shopOrderService.countByShop_IdAndStatus(shop.getId(), ShopOrderStatus.SHIPPING));
        numberOrderDetails.put("delivered", shopOrderService.countByShop_IdAndStatus(shop.getId(), ShopOrderStatus.DELIVERED));
        numberOrderDetails.put("cancelled", shopOrderService.countByShop_IdAndStatus(shop.getId(), ShopOrderStatus.CANCELLED));
        numberOrderDetails.put("processing", shopOrderService.countByShop_IdAndStatus(shop.getId(), ShopOrderStatus.PROCESSING));
        return ApiResponse.builder()
                .data(numberOrderDetails)
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






