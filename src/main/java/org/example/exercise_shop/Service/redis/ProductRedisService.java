package org.example.exercise_shop.Service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.ProductSortType;
import org.springframework.data.domain.Page;

public interface ProductRedisService {
    void clear(String name, int page, int size, ProductSortType sort, String categoryId);
    Page<ProductResponse> getProduct(String name, int page, int size, ProductSortType sort, String categoryId) throws JsonProcessingException;
    void cacheProductList(String name, int page, int size,ProductSortType sort, String categoryId, Page<ProductResponse> productPage);
}
