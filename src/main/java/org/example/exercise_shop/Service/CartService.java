package org.example.exercise_shop.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    Cart createNewCartForUser(String userId);
    void addToCart(String productId, int quantity, String userId, BigDecimal price);
    void addToCartCookies(AddToCartRequest addToCartRequest, HttpServletRequest request, HttpServletResponse response);
    void syncCartFromCookies(String cartData, String userId);
    void storeCartinCookies(HttpServletResponse response, List<AddToCartRequest> cartRequestList);
    void clearCartCookie(HttpServletResponse response);
    String getCartDataFromCookies(HttpServletRequest request);
    Cart findByUserId(String userId);
    void deleteCartItem(String productId);
    void deleteCartItemFromCookies(String productId, List<AddToCartRequest> addToCartRequests, HttpServletResponse response);

}
