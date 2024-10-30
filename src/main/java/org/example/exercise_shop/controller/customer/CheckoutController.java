package org.example.exercise_shop.controller.customer;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.OrderService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.OrderRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final OrderService orderService;

    @PostMapping("/create-order")
    public ApiResponse<Object> createOrders(@RequestBody OrderRequest orderRequest){
        orderService.createOrder(orderRequest);
        return ApiResponse.builder()
                .message("Create order successfully")
                .build();
    }

}
