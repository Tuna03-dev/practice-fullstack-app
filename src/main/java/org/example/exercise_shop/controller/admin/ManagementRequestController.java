package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.Shop;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class ManagementRequestController {

    private final ShopService shopService;

    @PostMapping("/approve-request")
    public ApiResponse<Shop> approveShop(@RequestBody Map<String, String> request){
        String shopId = request.get("shopId");
        Shop approveShop = shopService.approveShop(shopId, true);

        return ApiResponse.<Shop>builder()
                .data(approveShop)
                .build();
    }

    @PostMapping("/reject-request")
    public ApiResponse<Shop> rejectShop(@RequestBody Map<String, String> request){
        String shopId = request.get("shopId");
        Shop rejectShop = shopService.approveShop(shopId, false);

        return ApiResponse.<Shop>builder()
                .data(rejectShop)
                .build();
    }

}
