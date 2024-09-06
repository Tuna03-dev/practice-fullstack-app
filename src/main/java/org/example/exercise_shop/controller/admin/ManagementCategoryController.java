package org.example.exercise_shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.CategoryService;
import org.example.exercise_shop.dto.request.CategoryRequest;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/categories")
public class ManagementCategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ApiResponse<Category> addCategroy(@RequestBody CategoryRequest category){
        Category newCategory = categoryService.addCategory(category);
        return ApiResponse.<Category>builder()
                .data(newCategory)
                .build();
    }


}
