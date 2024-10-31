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
import org.example.exercise_shop.dto.response.CartItemResponse;
import org.example.exercise_shop.dto.response.CartResponse;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.mapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
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
                cartItem.setCartItemAmount(cartItem.getCartItemAmount().add(redisItem.getPricePerProduct().multiply(BigDecimal.valueOf(redisItem.getQuantity()))));
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
    public List<CartItemResponse> getCartItemResponsesFromCart(Cart cart) {
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


    @Override
    public Cart findByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> createNewCartForUser(userId));
    }

    @Override
    public void deleteCartItem(String productId, boolean isAuthenticated, String sessionId) {
        if (isAuthenticated){
            CartItem cartItem = cartItemRepository.findByProductId(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            cartItemRepository.delete(cartItem);
        }else {
            List<AddToCartRequest> cartItems = cartRedisService.getCart(sessionId);
            cartItems.removeIf(item -> item.getProductId().equals(productId));
            cartRedisService.cacheCart(sessionId, cartItems);
        }
    }

    @Override
    public List<CartItemResponse> getCartItemResponsesFromRedis(List<AddToCartRequest> addToCartRequestList) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (AddToCartRequest cartRequest : addToCartRequestList) {
            CartItemResponse cartItemResponse = cartItemMapper.addToCartRequestsToCartItemResponse(cartRequest);
            cartItemResponse.setCartItemAmount(cartRequest.getPricePerProduct().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
            Product product = productRepository.findById(cartRequest.getProductId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            cartItemResponse.setProductImage(product.getImage());
            cartItemResponse.setProductName(product.getName());
            cartItemResponse.setProductId(product.getId());
            cartItemResponses.add(cartItemResponse);
        }
        return cartItemResponses;
    }

    @Override
    public List<CartResponse> getListCartResponse(HttpServletRequest request, boolean isAuthenticated) {
        if (isAuthenticated) {
            return getAuthenticatedCartResponse();
        } else {
            return getGuestCartResponse(request);
        }
    }

    @Override
    public CartItemResponse updateCartItemQuantity(String productId, int quantity) {
        CartItem cartItem = cartItemRepository.findByProductId(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStockQuantity() < quantity) {
            throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
        }
        cartItem.setQuantity(quantity);
        cartItem.setCartItemAmount(cartItem.getPricePerProduct().multiply(BigDecimal.valueOf(quantity)));
        cartItemRepository.save(cartItem);
        return mapToCartItemResponse(cartItem);
    }

    @Override
    @Transactional
    public CartItemResponse updateCartItemQuantityFromRedis(String productId, int quantity, List<AddToCartRequest> addToCartRequestList, String sessionId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        if (product.getStockQuantity() < quantity) {
            throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
        }
        addToCartRequestList.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
        cartRedisService.cacheCart(sessionId, addToCartRequestList);
        return mapToCartItemResponseForGuest(addToCartRequestList.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    @Override
    @Transactional
    public void clearCart(String userId, String[] productIds) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new ApplicationException(ErrorCode.CART_NOT_FOUND));
        cart.getCartItems().removeIf(item -> Arrays.asList(productIds).contains(item.getProduct().getId()));
        cartRepository.save(cart);
    }

    @Override
    public void clearCartFromRedis(String sessionId, String[] productIds) {
        List<AddToCartRequest> cartItems = cartRedisService.getCart(sessionId);
        cartItems.removeIf(item -> Arrays.asList(productIds).contains(item.getProductId()));
        cartRedisService.cacheCart(sessionId, cartItems);
    }



    private List<CartResponse> getAuthenticatedCartResponse() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createNewCartForUser(user.getId()));

        return buildCartResponseFromItems(cart.getCartItems());
    }

    private List<CartResponse> getGuestCartResponse(HttpServletRequest request) {
        String sessionId = getSessionIdFromCookies(request, "SESSION_CART");
        if (sessionId == null) {
            return Collections.emptyList();
        }

        List<AddToCartRequest> cartItems = cartRedisService.getCart(sessionId);

        List<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(this::mapToCartItemResponseForGuest)
                .toList();

        return groupCartItemResponsesByShop(cartItemResponses);
    }

    private String getSessionIdFromCookies(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private CartItemResponse mapToCartItemResponseForGuest(AddToCartRequest cartItem) {
        Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        CartItemResponse cartItemResponse = cartItemMapper.addToCartRequestsToCartItemResponse(cartItem);
        cartItemResponse.setCartItemAmount(cartItem.getPricePerProduct().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemResponse.setProductImage(product.getImage());
        cartItemResponse.setProductName(product.getName());
        cartItemResponse.setProductId(product.getId());

        return cartItemResponse;
    }

    private List<CartResponse> buildCartResponseFromItems(Set<CartItem> cartItems) {
        List<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(this::mapToCartItemResponse)
                .toList();

        return groupCartItemResponsesByShop(cartItemResponses);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse cartItemResponse = cartItemMapper.cartItemsToCartItemResponse(cartItem);
        cartItemResponse.setProductId(cartItem.getProduct().getId());
        cartItemResponse.setProductName(cartItem.getProduct().getName());
        cartItemResponse.setProductImage(cartItem.getProduct().getImage());

        return cartItemResponse;
    }

    private List<CartResponse> groupCartItemResponsesByShop(List<CartItemResponse> cartItemResponses) {
        Map<String, CartResponse> cartResponseMap = new HashMap<>();

        for (CartItemResponse cartItemResponse : cartItemResponses) {
            Product product = productRepository.findById(cartItemResponse.getProductId()).orElse(null);
            if (product == null) continue;

            Shop shop = product.getShop();
            cartResponseMap.computeIfAbsent(shop.getId(), k ->
                    CartResponse.builder()
                            .shopId(shop.getId())
                            .shopName(shop.getName())
                            .cartItemResponses(new ArrayList<>())
                            .totalAmount(BigDecimal.ZERO)
                            .build()
            ).getCartItemResponses().add(cartItemResponse);

            cartResponseMap.get(shop.getId()).setTotalAmount(
                    cartResponseMap.get(shop.getId()).getTotalAmount().add(cartItemResponse.getCartItemAmount())
            );
        }

        return new ArrayList<>(cartResponseMap.values());
    }


}
