package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ShopInformationResponse;
import org.example.exercise_shop.entity.Shop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/shops")
public class ShopController {
    private static final Logger log = LoggerFactory.getLogger(ShopController.class);
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

    @GetMapping("/details/{productId}")
    public ApiResponse<ShopInformationResponse> getShopDetailByProductId(@PathVariable String productId){

        ShopInformationResponse shop = shopService.getShopDetail(productId);
        return ApiResponse.<ShopInformationResponse>builder()
                .data(shop)
                .build();
    }
}
