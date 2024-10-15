package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.CreateProductImageRequest;

import java.util.List;

public interface ProductImageService {
    void addImage(String productId, List<CreateProductImageRequest> createProductImageRequests);
}
