package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.CategoryRepository;
import org.example.exercise_shop.dto.request.CategoryRequest;
import org.example.exercise_shop.entity.Category;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public Category addCategory(CategoryRequest category) {
        Category newCategory = Category.builder()
                .name(category.getName())
                .build();

        return categoryRepository.save(newCategory);
    }
}
