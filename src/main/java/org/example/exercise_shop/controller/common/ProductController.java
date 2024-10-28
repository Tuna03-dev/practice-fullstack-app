package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.redis.ProductRedisService;
import org.example.exercise_shop.Service.ProductService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ProductDetailResponse;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.ProductSortType;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRedisService productRedisService;
    @GetMapping
    public ApiResponse<Page<ProductResponse>> showAllProducts(@RequestParam(name = "page", defaultValue = "0")int page,
                                                              @RequestParam(name = "size", defaultValue = "8")int size,
                                                              @RequestParam(name = "name", defaultValue = "")String name,
                                                              @RequestParam(name = "sort", defaultValue = "NONE") ProductSortType sort,
                                                              @RequestParam(name = "category", defaultValue = "") String categoryId){


        Page<ProductResponse> productPage = productService.getProducts(name, page, size, sort, categoryId);

        return ApiResponse.<Page<ProductResponse>>builder()
                .data(productPage)
                .build();

    }

    @GetMapping("/get-by-shopId/{shopId}")
    public ApiResponse<Page<ProductResponse>> getProductsByShopId(@PathVariable String shopId,
                                                                  @RequestParam(name = "page", defaultValue = "0")int page,
                                                                  @RequestParam(name = "size", defaultValue = "8")int size,
                                                                  @RequestParam(name = "name", defaultValue = "")String name,
                                                                  @RequestParam(name = "sort", defaultValue = "NONE") ProductSortType sort,
                                                                  @RequestParam(name = "category", defaultValue = "") String categoryId){
        Page<ProductResponse> products = productService.getProductsByShopId(name, page, size, sort, categoryId, shopId);

        return ApiResponse.<Page<ProductResponse>>builder()
                .data(products)
                .build();
    }


    @GetMapping("/get-all-by-shop/{shopId}")
    public ApiResponse<List<ProductResponse>> getAllProductsByShopId(@PathVariable String shopId){
        List<ProductResponse> products = productService.getAllProductsByShopId(shopId);

        return ApiResponse.<List<ProductResponse>>builder()
                .data(products)
                .build();
    }


    @GetMapping("/best-sellers")
    public ApiResponse<List<ProductResponse>> getBestSellers(@RequestParam(name = "size", defaultValue = "5") int size){
        List<ProductResponse> bestSellers = productService.getBestSellers(size);

        return ApiResponse.<List<ProductResponse>>builder()
                .data(bestSellers)
                .build();
    }

    @GetMapping("/new-arrival")
    public ApiResponse<List<ProductResponse>> getNewArrival(@RequestParam(name = "size", defaultValue = "5") int size){
        List<ProductResponse> bestSellers = productService.getNewArrival(size);

        return ApiResponse.<List<ProductResponse>>builder()
                .data(bestSellers)
                .build();
    }

    @GetMapping("/top-rates")
    public ApiResponse<List<ProductResponse>> getTopRates(@RequestParam(name = "size", defaultValue = "5") int size){
        List<ProductResponse> bestSellers = productService.getTopRates(size);

        return ApiResponse.<List<ProductResponse>>builder()
                .data(bestSellers)
                .build();
    }

    @GetMapping("/details/{productId}")
    public ApiResponse<ProductDetailResponse> getProductById(@PathVariable String productId){
        ProductDetailResponse product = productService.getProduct(productId);

        return ApiResponse.<ProductDetailResponse>builder()
                .data(product)
                .build();
    }
}
