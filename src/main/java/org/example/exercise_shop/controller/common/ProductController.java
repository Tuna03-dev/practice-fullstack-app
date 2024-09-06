package org.example.exercise_shop.controller.common;

import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ProductService;
import org.example.exercise_shop.dto.response.ApiResponse;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.ProductSortType;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<Product>> showAllProducts(@RequestParam(name = "page", defaultValue = "0")int page,
                                                     @RequestParam(name = "size", defaultValue = "10")int size,
                                                     @RequestParam(name = "name", defaultValue = "")String name,
                                                     @RequestParam(name = "sort", defaultValue = "NONE") ProductSortType sort){
        Page<Product> productPage = productService.getProducts(name, page, size, sort);

        return ApiResponse.<Page<Product>>builder()
                .data(productPage)
                .build();

    }
}
