package org.example.exercise_shop.controller.customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Service.CartService;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.dto.ApiResponse;
import org.example.exercise_shop.dto.response.CartItemResponse;
import org.example.exercise_shop.entity.Cart;
import org.example.exercise_shop.entity.CartItem;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.mapper.CartItemMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/carts")
public class CartController {
    private final CartService cartService;
    private final CartItemMapper cartItemMapper;

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getCart(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            Cart cart = cartService.findByUserId(user.getId());
            for (CartItem cartItem : cart.getCartItems()) {
                CartItemResponse cartItemResponse = cartItemMapper.cartItemsToCartItemResponse(cartItem);
                cartItemResponse.setProductId(cartItem.getProduct().getId());
                cartItemResponses.add(cartItemResponse);
            }

        } else{
            List<AddToCartRequest> addToCartRequestList = cartItemMapper.jsonToCartItems(cartService.getCartDataFromCookies(request));
            for (AddToCartRequest cartRequest : addToCartRequestList) {
                CartItemResponse cartItemResponse = cartItemMapper.addToCartRequestsToCartItemResponse(cartRequest);
                cartItemResponse.setCartItemAmount(cartRequest.getPricePerProduct().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
                cartItemResponses.add(cartItemResponse);
            }

        }
        return ApiResponse.<List<CartItemResponse>>builder()
                .data(cartItemResponses)
                .build();
    }


    @PostMapping("/add-to-cart")
    public ApiResponse<?> addToCart(@RequestBody AddToCartRequest addToCartRequest, HttpServletResponse response, HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            User user = (User) authentication.getPrincipal();
            cartService.addToCart(addToCartRequest.getProductId(), addToCartRequest.getQuantity(), user.getId(), addToCartRequest.getPricePerProduct());

        } else{
            cartService.addToCartCookies(addToCartRequest, request, response);
        }

        return ApiResponse.builder()
                .message("Add to cart successfully")
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<?> deleteCartItem(@PathVariable(value = "id")String productId, HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            cartService.deleteCartItem(productId);

        }else{
            List<AddToCartRequest> addToCartRequestList = cartItemMapper.jsonToCartItems(cartService.getCartDataFromCookies(request));
            cartService.deleteCartItemFromCookies(productId, addToCartRequestList, response);
        }


        return ApiResponse.builder()
                .message("Delete Successfully")
                .build();
    }

}
