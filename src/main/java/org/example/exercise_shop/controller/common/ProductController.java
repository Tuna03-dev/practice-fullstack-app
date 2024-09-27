package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ProductService;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.ProductResponse;
import org.example.exercise_shop.entity.ProductSortType;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductResponse>> showAllProducts(@RequestParam(name = "page", defaultValue = "0")int page,
                                                              @RequestParam(name = "size", defaultValue = "10")int size,
                                                              @RequestParam(name = "name", defaultValue = "")String name,
                                                              @RequestParam(name = "sort", defaultValue = "NONE") ProductSortType sort){
        Page<ProductResponse> productPage = productService.getProducts(name, page, size, sort);

        return ApiResponse.<Page<ProductResponse>>builder()
                .data(productPage)
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
}
