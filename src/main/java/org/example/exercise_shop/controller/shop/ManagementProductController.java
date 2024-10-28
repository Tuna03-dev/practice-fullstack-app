package org.example.exercise_shop.controller.shop;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.ProductService;
import org.example.exercise_shop.Service.ShopService;
import org.example.exercise_shop.dto.request.ProductCreationRequest;
import org.example.exercise_shop.dto.request.ProductUpdateRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shop/products")
public class ManagementProductController {
    private final ProductService productService;
    private final ShopService shopService;

    @GetMapping("/{id}")
    public ApiResponse<Page<Product>> getAllProducts(@PathVariable String id,
                                                     @RequestParam(name = "page", defaultValue = "0")int page,
                                                     @RequestParam(name = "size", defaultValue = "10")int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Shop shop = shopService.getShopById(id);
        if (!shop.getUserId().equals(user.getId())){
            return ApiResponse.<Page<Product>>builder()
                    .code(403)
                    .message("unauthorized")
                    .build();
        }
        Page<Product> productList = productService.getProductsByDeletedAtAndShopId(id, page,size);
        return ApiResponse.<Page<Product>>builder()
                .data(productList)
                .build();
    }
    
    @PostMapping("/add")
    public ApiResponse<Object> createProduct(@RequestBody @Valid ProductCreationRequest productCreationRequest) {
        Shop shop = shopService.findByCurrentUser();
       productService.addProduct(productCreationRequest, shop);


        return ApiResponse.<Object>builder()
                .message("Create successfully")
                .build();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Object> updateProduct(@RequestBody @Valid ProductCreationRequest productUpdateRequest, @PathVariable String id) {
        productService.updateProduct(productUpdateRequest, id);
        return ApiResponse.<Object>builder()
                .message("Update successfully")
                .build();
    }

    @DeleteMapping("/delete")
    public ApiResponse<String> deleteProduct(@RequestBody Map<String, String> request){
        Shop shop = shopService.findByCurrentUser();
        String productId = request.get("productId");
        productService.deleteProduct(productId, shop);
        return ApiResponse.<String>builder()
                .message("Delete successfully")
                .build();
    }

}
