package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.CategoryRequest;
import org.example.exercise_shop.dto.response.CategoryResponse;
import org.example.exercise_shop.entity.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(CategoryRequest category);
    List<CategoryResponse> getAllCategories();
}
