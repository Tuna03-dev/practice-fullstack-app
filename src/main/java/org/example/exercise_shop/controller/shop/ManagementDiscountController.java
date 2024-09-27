package org.example.exercise_shop.controller.shop;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.DiscountService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.CreateDiscountRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/discounts")
@RequiredArgsConstructor
public class ManagementDiscountController {
    private final DiscountService discountService;

    @PostMapping("/create")
    public ApiResponse<Object> createDiscount(@RequestBody CreateDiscountRequest createDiscountRequest) {

        try{
            discountService.createDiscount(createDiscountRequest);
        } catch (Exception e) {
            return ApiResponse.builder()
                    .message("Discount creation failed")
                    .build();
        }

        return ApiResponse.builder()
                .message("Discount created successfully")
                .build();
    }
}
