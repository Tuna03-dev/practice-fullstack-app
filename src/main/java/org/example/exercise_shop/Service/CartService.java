package org.example.exercise_shop.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.dto.response.CartItemResponse;
import org.example.exercise_shop.dto.response.CartResponse;
import org.example.exercise_shop.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    Cart createNewCartForUser(String userId);
    void addToCart(String productId, int quantity, String userId, BigDecimal price);
    void addToCartRedis(AddToCartRequest addToCartRequest,HttpServletRequest request, HttpServletResponse response);
    void syncCartFromRedis(String sessionId, String userId);
    List<CartItemResponse> getCartItemResponsesFromCart(Cart cart);
    Cart findByUserId(String userId);
    void deleteCartItem(String productId, boolean isAuthenticated, String sessionId);
    List<CartItemResponse> getCartItemResponsesFromRedis(List<AddToCartRequest> addToCartRequestList);
    List<CartResponse> getListCartResponse(HttpServletRequest request, boolean isAuthenticated);
    CartItemResponse updateCartItemQuantity(String productId, int quantity);
    CartItemResponse updateCartItemQuantityFromRedis(String productId, int quantity, List<AddToCartRequest> addToCartRequestList, String sessionId);
    void clearCart(String userId, String[] productIds);
    void clearCartFromRedis(String sessionId, String[] productIds);

}
