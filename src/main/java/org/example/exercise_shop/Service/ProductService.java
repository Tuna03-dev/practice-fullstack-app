package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.ProductCreationRequest;
import org.example.exercise_shop.dto.request.ProductUpdateRequest;
import org.example.exercise_shop.dto.response.ProductDetailResponse;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.ProductSortType;
import org.example.exercise_shop.entity.Shop;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    Page<ProductResponse> getProducts(String name, int page, int size, ProductSortType productSortType, String categoryId);
    Page<Product> getProductsByDeletedAtAndShopId(String shopId, int page, int size);
    ProductDetailResponse getProduct(String id);
    Product addProduct(ProductCreationRequest productCreationRequest, Shop shop);
    Product updateProduct(ProductUpdateRequest productUpdateRequest);
    void deleteProduct(String productId, Shop shop);

    List<ProductResponse> getBestSellers(int size);
    List<ProductResponse> getNewArrival(int size);
    List<ProductResponse> getTopRates(int size);

    Product findById(String productId);
    List<ProductResponse> getRecommendProducts(String shopId);
}
