package org.example.exercise_shop.Service;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.*;
import org.example.exercise_shop.dto.request.OrderRequest;
import org.example.exercise_shop.dto.request.ShopOrderRequest;
import org.example.exercise_shop.dto.response.OrderItemResponse;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.dto.response.ShopOrderResponse;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.exception.OutOfStockException;
import org.example.exercise_shop.mapper.OrderItemMapper;
import org.example.exercise_shop.mapper.OrderMapper;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService{

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImp.class);
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartitemRepository cartitemRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final AddressRepository addressRepository;
    private final ShopRepository shopRepository;
    private final ShopService shopService;
    private final OrderItemMapper orderItemMapper;
    private final ShopOrderRepository shopOrderRepository;

    @Override
    @Transactional
    public Page<ShopOrderResponse> findAllByUserId(String userId, int page, int size, String type, String search) {
        List<Order> orders = orderRepository.findOrdersWithDetailsByUserId(userId);
        List<ShopOrderResponse> shopOrderResponses = orders.stream()
                .flatMap(order -> responseShopOrder(order, type, search).stream())
                .toList();

        int start = (int) Pageable.ofSize(size).withPage(page).getOffset();
        int end = Math.min((start + size), shopOrderResponses.size());
        List<ShopOrderResponse> pagedShopOrderResponses = shopOrderResponses.subList(start, end);
        return new PageImpl<>(pagedShopOrderResponses, PageRequest.of(page, size), shopOrderResponses.size());
    }


    @Override
    public Page<ShopOrderResponse> findAllByShopId(String shopId, int page, int size, ShopOrderStatus status, String search, String delivery) {
        List<Order> orders = orderRepository.findOrderWithDetailsByShopId(shopId, status, delivery);
        log.info("status: {}", status);
        List<ShopOrderResponse> shopOrderResponses = orders.stream().flatMap(order -> responseShopOrder(order, "", search).stream()).toList();
        int start = (int) Pageable.ofSize(size).withPage(page).getOffset();
        int end = Math.min((start + size), shopOrderResponses.size());
        List<ShopOrderResponse> pagedShopOrderResponses = shopOrderResponses.subList(start, end);
        return new PageImpl<>(pagedShopOrderResponses, PageRequest.of(page, size), shopOrderResponses.size());
    }




    @Override
    public BigDecimal handlerCheckoutInfor(OrderRequest orderRequest) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        BigDecimal totalAmount = BigDecimal.ZERO;
//        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
//        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
//        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> orderRequest.getCartItemIds().contains(item.getId())).toList();
//
//
//
//        for (CartItem item : selectedCartItems){
//            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
//            totalAmount = totalAmount.add(item.getCartItemAmount());
//            synchronized (product){
//                LocalDateTime now = LocalDateTime.now();
//                if (now.minusSeconds(timeReserveProduct).isAfter(item.getReserveAt())){
//                    if (product.getStockQuantity() < item.getQuantity()){
//                        throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
//                    }
//                }
//
//            }
//
//        }

//        return totalAmount;
        return null;
    }

    @Override
    @Transactional
    public void saveOrder(OrderRequest orderRequest, BigDecimal totalAmount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
//        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> orderRequest.getCartItemIds().contains(item.getId())).toList();
//        Shop shop = selectedCartItems.stream().findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)).getProduct().getShop();
        Order order = orderMapper.orderRequestToOrder(orderRequest);
        order.setStatus(StatusOrder.PENDING);
        order.setTotalAmount(totalAmount);
        order.setUser(user);
//        order.setShop(shop);
        order = orderRepository.save(order);


//        for (CartItem item : selectedCartItems){
//            Product product = item.getProduct();
//            OrderItem orderItem = OrderItem.builder()
//                    .pricePerUnit(item.getPricePerProduct())
//                    .quantity(item.getQuantity())
//                    .totalAmount(item.getCartItemAmount())
//                    .product(product)
//                    .build();
//            synchronized (product){
//                LocalDateTime now = LocalDateTime.now();
//                if (now.minusSeconds(timeReserveProduct).isAfter(item.getReserveAt())){
//                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
//                    product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());
//                }else{
//                    product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());
//
//                }
//            }
//            cartitemRepository.delete(item);
////            orderItem.setOrder(order);
//            orderItemRepository.save(orderItem);
//        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void createOrder(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
        List<String> selectedCartItemIds = orderRequest.getShopOrderRequests().stream().flatMap(shop -> shop.getCartItemIds().stream()).toList();
        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> selectedCartItemIds.contains(item.getId())).toList();
        processOrderInformation(selectedCartItems);
        Address address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(() -> new ApplicationException(ErrorCode.ADDRESS_NOT_FOUND));

        Order order = new Order();
        order.setStatus(StatusOrder.PENDING);
        order.setUser(user);
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setTotalAmountPaid(orderRequest.getTotalAmountPaid());
        order.setAddress(address);
        Set<ShopOrder> shopOrders = new HashSet<>();
        for (ShopOrderRequest shopOrderRequest : orderRequest.getShopOrderRequests()) {
            ShopOrder shopOrder = createShopOrder(shopOrderRequest, selectedCartItems, order);
            shopOrders.add(shopOrder);
        }
        order.setShopOrders(shopOrders);
        orderRepository.save(order);
    }


    @Override
    public void cancelOrder(String orderId) {
        ShopOrder shopOrder = shopOrderRepository.findById(orderId).orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));
        if (shopOrder.getStatus() != ShopOrderStatus.PENDING && shopOrder.getStatus() != ShopOrderStatus.PROCESSING ) throw new ApplicationException(ErrorCode.ORDER_CANNOT_CANCEL);
        shopOrder.setStatus(ShopOrderStatus.CANCELLED);
        shopOrderRepository.save(shopOrder);
    }

    private Set<ShopOrderResponse> responseShopOrder(Order order, String type, String search) {
        Set<ShopOrder> shopOrders = order.getShopOrders();
        if (order.getUser() == null) throw new ApplicationException(ErrorCode.USER_NOT_FOUND);
        User user = order.getUser();
        shopOrders = switch (type) {
            case "pending" -> shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getStatus() == ShopOrderStatus.PENDING)
                    .collect(Collectors.toSet());
            case "processing" -> shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getStatus() == ShopOrderStatus.PROCESSING)
                    .collect(Collectors.toSet());
            case "delivered" -> shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getStatus() == ShopOrderStatus.DELIVERED)
                    .collect(Collectors.toSet());
            case "cancelled" -> shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getStatus() == ShopOrderStatus.CANCELLED)
                    .collect(Collectors.toSet());
            case "delivering" -> shopOrders.stream()
                    .filter(shopOrder -> shopOrder.getStatus() == ShopOrderStatus.SHIPPING)
                    .collect(Collectors.toSet());
            default -> shopOrders;
        };
        return shopOrders.stream().map(shopOrder -> {

            Shop shop = shopOrder.getShop();
            ShopOrderResponse shopOrderResponse = orderMapper.toShopOrderResponse(shopOrder);
            shopOrderResponse.setShopInformationResponse(shopService.getShopDetailByShopId(shop.getId()));
            shopOrderResponse.setOrderItems(responseOrderItemResponse(shopOrder, search));
            shopOrderResponse.setAddress(order.getAddress());
            shopOrderResponse.setUsername(user.getUsername());
            shopOrderResponse.setOrderId(order.getId());
            return shopOrderResponse;
        }).filter(shopOrderResponse -> !shopOrderResponse.getOrderItems().isEmpty()).collect(Collectors.toSet());
    }


    private Set<OrderItemResponse> responseOrderItemResponse(ShopOrder shopOrder, String search){
        Set<OrderItem> orderItems = shopOrder.getOrderItems().stream().
                filter(orderItem ->
                    orderItem.getProduct().getName().toLowerCase().contains(search.toLowerCase()))
                .collect(Collectors.toSet());

        return orderItems.stream().map(orderItem -> {
            Product product = orderItem.getProduct();
            OrderItemResponse orderItemResponse = orderItemMapper.toOrderItemResponse(orderItem);
            orderItemResponse.setProductId(product.getId());
            orderItemResponse.setProductName(product.getName());
            orderItemResponse.setProductImageUrl(product.getImage());
            return orderItemResponse;
        }).collect(Collectors.toSet());
    }


    private ShopOrder createShopOrder(ShopOrderRequest shopOrderRequest, List<CartItem> selectedCartItems, Order order) {
        Shop shop = shopRepository.findById(shopOrderRequest.getShopId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.SHOP_NOT_FOUND));

        ShopOrder shopOrder = new ShopOrder();
        shopOrder.setShop(shop);
        shopOrder.setShippingMethod(shippingMethodRepository.findById(shopOrderRequest.getShippingMethodId())
                .orElseThrow(() -> new ApplicationException(ErrorCode.SHIPPING_METHOD_NOT_FOUND)));
        shopOrder.setTotalAmount(shopOrderRequest.getTotalAmount());
        shopOrder.setStatus(ShopOrderStatus.PENDING);
        shopOrder.setOrder(order);
        shopOrder.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(shopOrderRequest.getTimeDelivery()));

        Set<OrderItem> orderItems = shopOrderRequest.getCartItemIds().stream()
                .map(cartItemId -> createOrderItem(cartItemId, selectedCartItems, shopOrderRequest.getTimeDelivery(), shopOrder))
                .collect(Collectors.toSet());

        shopOrder.setOrderItems(orderItems);
        return shopOrder;
    }

    private OrderItem createOrderItem(String cartItemId, List<CartItem> selectedCartItems, int deliveryTime, ShopOrder shopOrder) {
        CartItem cartItem = selectedCartItems.stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));

        Product product = cartItem.getProduct();
        OrderItem orderItem = OrderItem.builder()
                .pricePerUnit(cartItem.getPricePerProduct())
                .quantity(cartItem.getQuantity())
                .totalAmount(cartItem.getCartItemAmount())
                .product(product)
                .build();

        synchronized (product) {
            int updatedStock = product.getStockQuantity() - cartItem.getQuantity();
            if (updatedStock < 0) throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
            product.setStockQuantity(updatedStock);
            product.setSoldQuantity(product.getSoldQuantity() + cartItem.getQuantity());
        }

        cartitemRepository.delete(cartItem);
        orderItem.setShopOrder(shopOrder);
        orderItemRepository.save(orderItem);

        return orderItem;
    }

    private void processOrderInformation(List<CartItem> selectedCartItems) {
        Map<String, String> outOfStockProducts = new HashMap<>();


        for (CartItem item : selectedCartItems){
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            synchronized (product){
                if (product.getStockQuantity() < item.getQuantity()){
                    outOfStockProducts.put(item.getId(),"Product "+ product.getName() +" has only " + item.getQuantity() + " items in stock. Please adjust your order.");
                }
            }

        }

        if (!outOfStockProducts.isEmpty()){
            throw new OutOfStockException(outOfStockProducts);
        }

    }
}
