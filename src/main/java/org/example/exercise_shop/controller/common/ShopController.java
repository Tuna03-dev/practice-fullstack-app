package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/shops")
public class ShopController {
    private final ShopService shopService;

    @GetMapping
    public ApiResponse<Page<Shop>> getAllShops(@RequestParam(name = "page", defaultValue = "0") int page,
                                               @RequestParam(name = "size", defaultValue = "10") int size,
                                               @RequestParam(name = "category", required = false) String categoryId,
                                               @RequestParam(name = "name", required = false) String name,
                                               @RequestParam(name = "address", required = false) String address){


        Page<Shop> shopPage = shopService.getAllShopsActive(page, size, name, categoryId, address);
        return ApiResponse.<Page<Shop>>builder()
                .data(shopPage)
                .build();
    }
}
