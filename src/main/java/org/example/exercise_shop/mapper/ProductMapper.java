package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.ProductCreationRequest;
import org.example.exercise_shop.dto.response.ProductDetailResponse;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponse toProductResponse(Product product);
    ProductDetailResponse toProductDetailResponse(Product product);
    Product toProduct(ProductCreationRequest productCreationRequest);
    void updateProductFromRequest(@MappingTarget Product product, ProductCreationRequest productCreationRequest);
}
