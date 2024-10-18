package org.example.exercise_shop.controller.shop;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ProductImageService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.request.CreateProductImageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/shop/products/images")
@RestController
@RequiredArgsConstructor
public class ManagementProductImageController {

    private final ProductImageService productImageService;

    @PostMapping("/add/{productId}")
    public ApiResponse<Object> addListImage(@PathVariable String productId, @RequestBody List<CreateProductImageRequest> createProductImageRequests) {
        productImageService.addImage(productId, createProductImageRequests);
        return ApiResponse.builder()
                .message("Add image successfully")
                .build();
    }

}
