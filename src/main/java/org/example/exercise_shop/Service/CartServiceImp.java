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
import org.example.exercise_shop.dto.request.AddToCartRequest;
import org.example.exercise_shop.entity.Cart;
import org.example.exercise_shop.entity.CartItem;
import org.example.exercise_shop.entity.Product;
import org.example.exercise_shop.entity.User;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.CartItemMapper;
import org.example.exercise_shop.utils.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImp implements CartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;
    private final CartitemRepository cartItemRepository;
    private final TaskScheduler taskScheduler;

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
        synchronized (product){
            if (product.getStockQuantity() < quantity){
                throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            }
            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepository.save(product);
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

        Trigger trigger = triggerContext -> {
            Date now = new Date();
            return new Date(now.getTime() + (timeReseverProduct + 1)*1000).toInstant();
        };

        taskScheduler.schedule(() -> this.checkAndReturnStock(cartItem), trigger);

    }

    @Override
    @Transactional
    public void addToCartCookies(AddToCartRequest addToCartRequest, HttpServletRequest request, HttpServletResponse response) {
        String cartData = getCartDataFromCookies(request);
        List<AddToCartRequest> cartItems = cartItemMapper.jsonToCartItems(cartData);
        log.info(cartItems.stream().toString());
        Product product = productRepository.findById(addToCartRequest.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        synchronized (product){
            if (product.getStockQuantity() < addToCartRequest.getQuantity()){
                throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            }
            product.setStockQuantity(product.getStockQuantity() - addToCartRequest.getQuantity());
            productRepository.save(product);
        }
        Optional<AddToCartRequest> existingItemOptional = cartItems.stream()
                .filter(item -> item.getProductId().equals(addToCartRequest.getProductId()))
                .findFirst();

        if (existingItemOptional.isPresent()) {
            AddToCartRequest existingItem = existingItemOptional.get();
            existingItem.setQuantity(existingItem.getQuantity() + addToCartRequest.getQuantity());
        } else {
            try{
                cartItems.add(addToCartRequest);
            }catch (UnsupportedOperationException e){
                throw new UnsupportedOperationException(e.getMessage());
            }

        }
        storeCartinCookies(response, cartItems);
    }

    @Override
    @Transactional
    public void syncCartFromCookies(String cartData, String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> createNewCartForUser(userId));

        List<AddToCartRequest> cookieItems = cartItemMapper.jsonToCartItems(cartData);

        synchronized (this){
            for (AddToCartRequest cookieItem : cookieItems) {
                Optional<CartItem> existingItem = cart.getCartItems().stream()
                        .filter(item -> item.getProduct().getId().equals(cookieItem.getProductId())).findFirst();

                if (existingItem.isPresent()){
                    CartItem cartItem = existingItem.get();
                    cartItem.setQuantity(cartItem.getQuantity() + cookieItem.getQuantity());
                    cartItem.setPricePerProduct(cookieItem.getPricePerProduct());
                }else{
                    Product product = productRepository.findById(cookieItem.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
                    CartItem newItem = cartItemMapper.addToCartRequestToCartItem(cookieItem);
                    newItem.setProduct(product);
                    newItem.setCart(cart);
                    newItem.setCartItemAmount(newItem.getPricePerProduct().multiply(BigDecimal.valueOf(newItem.getQuantity())));
                    cart.getCartItems().add(newItem);
                }
            }
            cartRepository.save(cart);

        }

    }

    @Override
    public void storeCartinCookies(HttpServletResponse response, List<AddToCartRequest> cartRequestList) {
        String data = cartItemMapper.cartItemsToJson(cartRequestList);
        String encodeData = CookieUtils.encodeValue(data);
        Cookie cookie = new Cookie("cart", encodeData);
        cookie.setMaxAge(7*24*60*60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public void clearCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("cart", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    @Override
    public String getCartDataFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies){
                if ("cart".equals(cookie.getName())){
                    return CookieUtils.decodeValue(cookie.getValue());
                }
            }
        }
        return null;
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

    @Override
    public void deleteCartItemFromCookies(String productId, List<AddToCartRequest> addToCartRequests, HttpServletResponse response) {
        addToCartRequests.removeIf(item -> item.getProductId().equals(productId));
        storeCartinCookies(response,addToCartRequests);
    }


    private void checkAndReturnStock(CartItem cartItem){
        LocalDateTime now = LocalDateTime.now();
        if (now.minusSeconds(timeReseverProduct).isAfter(cartItem.getReserveAt())){
            Product product = productRepository.findById(cartItem.getProduct().getId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            if (product != null){
                synchronized (product){
                    product.setStockQuantity(product.getStockQuantity() + cartItem.getQuantity());
                    productRepository.save(product);
                }

            }
        }



    }


}
