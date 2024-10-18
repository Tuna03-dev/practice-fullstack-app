package org.example.exercise_shop.Service;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.CategoryRepository;
import org.example.exercise_shop.dto.request.CategoryRequest;
import org.example.exercise_shop.dto.response.CategoryResponse;
import org.example.exercise_shop.entity.Category;
import org.example.exercise_shop.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService{

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category addCategory(CategoryRequest category) {
        Category newCategory = Category.builder()
                .name(category.getName())
                .build();

        return categoryRepository.save(newCategory);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesByShopId(String shopId) {
        List<Category> categories = categoryRepository.findAllByShopId(shopId);

        return categories.stream().map(categoryMapper::toCategoryResponse).toList();
    }
}
