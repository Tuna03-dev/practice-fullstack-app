package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.CategoryService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.CategoryResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getAllCategories())
                .build();
    }

    @GetMapping("/get-by-shop/{shopId}")
    public ApiResponse<List<CategoryResponse>> getCategoriesByShopId(@PathVariable String shopId) {
        return ApiResponse.<List<CategoryResponse>>builder()
                .data(categoryService.getCategoriesByShopId(shopId))
                .build();
    }
}
