package org.example.exercise_shop.Service;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exercise_shop.Repository.CartRepository;
import org.example.exercise_shop.Repository.CartitemRepository;
import org.example.exercise_shop.Repository.ProductRepository;
import org.example.exercise_shop.Repository.UserRepository;
import org.example.exercise_shop.Service.redis.CartRedisService;
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.entity.Cart;
import org.example.exercise_shop.entity.CartItem;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImp implements CartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final CartitemRepository cartItemRepository;
    private final CartRedisService cartRedisService;

    @Value(value = "${application.time-reserve-product}")
    private long timeReseverProduct;

    @Override
    public Cart createNewCartForUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ApplicationException(ErrorCode.USER_NOT_FOUND));
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void addToCart(String productId, int quantity, String userId, BigDecimal price) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCartForUser(userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        synchronized (product) {
            int newQuantity = quantity;
            CartItem cartItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst()
                    .orElse(null);

            if (cartItem != null) {
                newQuantity += cartItem.getQuantity();
            }
            if (product.getStockQuantity() < newQuantity) {
                throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            }
        }

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(()->{
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProduct(product);
                    newItem.setPricePerProduct(price);
                    cart.getCartItems().add(newItem);
                    return newItem;
                });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItem.setCartItemAmount((price.multiply(BigDecimal.valueOf(quantity))).add(cartItem.getCartItemAmount()));
        cartRepository.save(cart);

//        Trigger trigger = triggerContext -> {
//            Date now = new Date();
//            return new Date(now.getTime() + (timeReseverProduct + 1)*1000).toInstant();
//        };
//
//        taskScheduler.schedule(() -> this.checkAndReturnStock(cartItem), trigger);

    }

    @Override
    @Transactional
    public void addToCartRedis(AddToCartRequest addToCartRequest, HttpServletRequest request, HttpServletResponse response) {
        log.info("Add to cart request: {}", addToCartRequest.getProductId());
        String sessionId = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("SESSION_CART") && cookie.getValue() != null)
                .findFirst().orElseGet(() -> {
                    Cookie cookie = new Cookie("SESSION_CART", UUID.randomUUID().toString());
                    cookie.setPath("/");
                    cookie.setHttpOnly(true);
                    cookie.setSecure(true);
                    cookie.setMaxAge(60*60*24*30);
                    response.addCookie(cookie);
                    return cookie;
                }).getValue();

        log.info("Session id: {}", sessionId);
        List<AddToCartRequest> cartItems = new ArrayList<>();
        cartItems = cartRedisService.getCart(sessionId);

        Product product = productRepository.findById(addToCartRequest.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        synchronized (product){
            if (product.getStockQuantity() < addToCartRequest.getQuantity()){
                throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            }
        }
        Optional<AddToCartRequest> existingItemOptional = cartItems.stream()
                .filter(item -> item.getProductId().equals(addToCartRequest.getProductId()))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            AddToCartRequest existingItem = existingItemOptional.get();
            if (product.getStockQuantity() < existingItem.getQuantity() + addToCartRequest.getQuantity()) {
                throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            }
            existingItem.setQuantity(existingItem.getQuantity() + addToCartRequest.getQuantity());

        } else {
            cartItems.add(addToCartRequest);
        }
        cartRedisService.cacheCart(sessionId, cartItems);
    }

    @Override
    @Transactional
    public void syncCartFromRedis(String sessionId, String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCartForUser(userId));

        List<AddToCartRequest> redisCartItems = cartRedisService.getCart(sessionId);
        for (AddToCartRequest redisItem : redisCartItems) {
            Optional<CartItem> existingItem = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(redisItem.getProductId())).findFirst();

            if (existingItem.isPresent()){
                CartItem cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + redisItem.getQuantity());
                cartItem.setPricePerProduct(redisItem.getPricePerProduct());
            }else{
                Product product = productRepository.findById(redisItem.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
                CartItem newItem = cartItemMapper.addToCartRequestToCartItem(redisItem);
                newItem.setProduct(product);
                newItem.setCart(cart);
                newItem.setCartItemAmount(newItem.getPricePerProduct().multiply(BigDecimal.valueOf(newItem.getQuantity())));
                cart.getCartItems().add(newItem);
            }
        }
        cartRepository.save(cart);
        cartRedisService.clear(sessionId);

    }



    @Override
    public Cart findByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> createNewCartForUser(userId));
    }

    @Override
    public void deleteCartItem(String productId) {
        CartItem item = cartItemRepository.findByProductId(productId).orElseThrow(() -> new  ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        cartItemRepository.delete(item);
    }






}
