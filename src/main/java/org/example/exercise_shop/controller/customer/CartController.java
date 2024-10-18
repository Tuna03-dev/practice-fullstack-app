package org.example.exercise_shop.controller.customer;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Service.CartService;
import org.example.exercise_shop.Service.ProductService;
import org.example.exercise_shop.Service.redis.CartRedisService;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.CartItemResponse;
import org.example.exercise_shop.dto.response.CartResponse;
import org.example.exercise_shop.entity.Cart;
import org.example.exercise_shop.entity.CartItem;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.mapper.CartItemMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/carts")
public class CartController {
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;
    private final CartRedisService cartRedisService;
    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        if (isAuthenticated(authentication)) {
            User user = (User) authentication.getPrincipal();
            Cart cart = cartService.findByUserId(user.getId());
            cartItemResponses = cartService.getCartItemResponsesFromCart(cart);
        } else {
            String sessionId = getSessionIdFromCookies(request);
            List<AddToCartRequest> addToCartRequestList = cartRedisService.getCart(sessionId);
            cartItemResponses = cartService.getCartItemResponsesFromRedis(addToCartRequestList);
        }

        return ApiResponse.<List<CartItemResponse>>builder()
                .data(cartItemResponses)
                .build();
    }

    @GetMapping("/get-cart")
    public ApiResponse<List<CartResponse>> getListCartResponse(HttpServletRequest request){
        List<CartResponse> cartResponses = cartService.getListCartResponse(request,isAuthenticated(SecurityContextHolder.getContext().getAuthentication()));
        return ApiResponse.<List<CartResponse>>builder()
                .data(cartResponses)
                .build();
    }

    @PutMapping("/update-quantity/{id}")
    public ApiResponse<Object> updateQuantity(@PathVariable(value = "id")String productId, @RequestBody Map<String, Integer> requestBody, HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CartItemResponse cartItemResponse = new CartItemResponse();
        try{
            if (isAuthenticated(authentication)){
                cartItemResponse = cartService.updateCartItemQuantity(productId, requestBody.get("quantity"));
            }else{
                List<AddToCartRequest> addToCartRequestList = cartRedisService.getCart(getSessionIdFromCookies(request));
                cartItemResponse = cartService.updateCartItemQuantityFromRedis(productId, requestBody.get("quantity"), addToCartRequestList, getSessionIdFromCookies(request));
            }
        }catch (Exception e){
            Product product = productService.findById(productId);
            return ApiResponse.builder()
                    .message(String.format("Sorry, you can only buy up to %s products", product.getStockQuantity()))
                    .data(product.getStockQuantity())
                    .build();
        }
        return ApiResponse.builder()
                .data(cartItemResponse)
                .build();
    }


    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    private String getSessionIdFromCookies(HttpServletRequest request) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "SESSION_CART".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }




    @PostMapping("/add-to-cart")
    public ApiResponse<Object> addToCart(@RequestBody AddToCartRequest addToCartRequest, HttpServletResponse response, HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            cartService.addToCart(addToCartRequest.getProductId(), addToCartRequest.getQuantity(), user.getId(), addToCartRequest.getPricePerProduct());

        } else{
            cartService.addToCartRedis(addToCartRequest, request, response);
        }

        return ApiResponse.builder()
                .message("Add to cart successfully")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<Object> deleteCartItem(@PathVariable(value = "id")String productId, HttpServletRequest request){
        boolean isAuthenticated = isAuthenticated(SecurityContextHolder.getContext().getAuthentication());

        cartService.deleteCartItem(productId,isAuthenticated, getSessionIdFromCookies(request));

        return ApiResponse.builder()
                .message("Delete Successfully")
                .build();
    }

    @DeleteMapping("/clear")
    public ApiResponse<Object> clearCart(@RequestBody String[] productIds, HttpServletRequest request){
        boolean isAuthenticated = isAuthenticated(SecurityContextHolder.getContext().getAuthentication());

        if (isAuthenticated){
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            cartService.clearCart(user.getId(), productIds);
        }else{
            cartService.clearCartFromRedis(getSessionIdFromCookies(request), productIds);
        }

        return ApiResponse.builder()
                .message("Clear cart successfully")
                .build();
    }

}
