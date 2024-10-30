package org.example.exercise_shop.controller;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShippingMethodService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.ShippingMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shipping-method")
@RequiredArgsConstructor
public class ShippingMethodController {

    private final ShippingMethodService shippingMethodService;

    @GetMapping
    public ApiResponse<List<ShippingMethod>> getALlShippingMethod() {
        List<ShippingMethod> shippingMethods = shippingMethodService.getAllShippingMethod();
        return ApiResponse.<List<ShippingMethod>>builder()
                .data(shippingMethods)
                .build();
    }
}
