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
            cartItemResponses = getCartItemResponsesFromCart(cart);
        } else {
            String sessionId = getSessionIdFromCookies(request);
            List<AddToCartRequest> addToCartRequestList = cartRedisService.getCart(sessionId);
            cartItemResponses = getCartItemResponsesFromRedis(addToCartRequestList);
        }

        return ApiResponse.<List<CartItemResponse>>builder()
                .data(cartItemResponses)
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

    private List<CartItemResponse> getCartItemResponsesFromCart(Cart cart) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            CartItemResponse cartItemResponse = cartItemMapper.cartItemsToCartItemResponse(cartItem);
            cartItemResponse.setProductId(cartItem.getProduct().getId());
            cartItemResponse.setProductName(cartItem.getProduct().getName());
            cartItemResponse.setProductImage(cartItem.getProduct().getImage());
            cartItemResponses.add(cartItemResponse);
        }
        return cartItemResponses;
    }

    private List<CartItemResponse> getCartItemResponsesFromRedis(List<AddToCartRequest> addToCartRequestList) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (AddToCartRequest cartRequest : addToCartRequestList) {
            CartItemResponse cartItemResponse = cartItemMapper.addToCartRequestsToCartItemResponse(cartRequest);
            cartItemResponse.setCartItemAmount(cartRequest.getPricePerProduct().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            Product product = productService.findById(cartRequest.getProductId());
            cartItemResponse.setProductImage(product.getImage());
            cartItemResponse.setProductName(product.getName());
            cartItemResponse.setProductId(product.getId());
            cartItemResponses.add(cartItemResponse);
        }
        return cartItemResponses;
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

//    @DeleteMapping("/delete/{id}")
//    public ApiResponse<Object> deleteCartItem(@PathVariable(value = "id")String productId, HttpServletRequest request, HttpServletResponse response){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null &&authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
//            cartService.deleteCartItem(productId);
//
//        }else{
//            List<AddToCartRequest> addToCartRequestList = cartItemMapper.jsonToCartItems(cartService.getCartDataFromCookies(request));
//            cartService.deleteCartItemFromCookies(productId, addToCartRequestList, response);
//        }
//
//
//        return ApiResponse.builder()
//                .message("Delete Successfully")
//                .build();
//    }

}
