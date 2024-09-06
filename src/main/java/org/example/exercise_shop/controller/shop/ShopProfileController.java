package org.example.exercise_shop.controller.shop;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.request.ShopUpdateRequest;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/info")
public class ShopProfileController {

    private final ShopService shopService;

    @GetMapping("/{id}")
    public ApiResponse<Shop> getShopInformation(@PathVariable(value = "id") String shopId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Shop shop = shopService.getShopById(shopId);
        if (!shop.getUserId().equals(user.getId())){
            return ApiResponse.<Shop>builder()
                    .code(403)
                    .message("You do not have permission to access this resource")
                    .build();
        }
        return ApiResponse.<Shop>builder()
                .data(shop)
                .build();

    }

    @PutMapping("/update")
    public ApiResponse<Shop> updateShopInformation(@RequestBody ShopUpdateRequest shopUpdateRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Shop shop = shopService.updateShop(shopUpdateRequest);
        if (!shop.getUserId().equals(user.getId())){
            return ApiResponse.<Shop>builder()
                    .code(403)
                    .message("You do not have permission to access this resource")
                    .build();
        }
        return ApiResponse.<Shop>builder()
                .data(shop)
                .build();

    }
}
