package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.response.CategoryResponse;
import org.example.exercise_shop.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

}
