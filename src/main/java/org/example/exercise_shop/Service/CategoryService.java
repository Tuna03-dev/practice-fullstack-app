package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.CategoryRequest;
import org.example.exercise_shop.entity.Category;

public interface CategoryService {
    Category addCategory(CategoryRequest category);

}
