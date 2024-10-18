package org.example.exercise_shop.mapper;

import org.example.exercise_shop.dto.request.CreateProductImageRequest;
import org.example.exercise_shop.dto.response.ProductImageResponse;
import org.example.exercise_shop.entity.ProductImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    List<ProductImage> toListProductImage(List<CreateProductImageRequest> createProductImageRequests);
    List<ProductImageResponse> toListProductImageResponse(List<ProductImage> productImages);
}
