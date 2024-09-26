package org.example.exercise_shop.controller.shop;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.request.ShopCreationRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.Shop;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop")
public class RequestCreateShopController {

    private final ShopService shopService;

    @PostMapping("/create")
    public ApiResponse<Shop> requestCreateShop(@RequestBody ShopCreationRequest shopCreationRequest){
        Shop requestShop = shopService.requestShop(shopCreationRequest);
        return ApiResponse.<Shop>builder()
                .data(requestShop)
                .build();

    }


}
