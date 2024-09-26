package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ShopFeeService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.ShopFee;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/profit-fee")
public class ManagementShopFeeController {

    private final ShopFeeService shopFeeService;

    @PutMapping
    public ApiResponse<ShopFee> setShopfee(@RequestBody ShopFee shopFee){
        shopFee.setEffectiveDate(LocalDate.now().plusMonths(1));
        ShopFee newShopFee = shopFeeService.addShopFee(shopFee);

        return ApiResponse.<ShopFee>builder()
                .data(newShopFee)
                .build();

    }
}
