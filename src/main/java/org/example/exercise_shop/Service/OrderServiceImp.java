package org.example.exercise_shop.Service;


import lombok.RequiredArgsConstructor;
import org.example.exercise_shop.Repository.*;
import org.example.exercise_shop.dto.request.OrderRequest;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.entity.*;
import org.example.exercise_shop.exception.ApplicationException;
import org.example.exercise_shop.exception.ErrorCode;
import org.example.exercise_shop.exception.OutOfStockException;
import org.example.exercise_shop.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService{

    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartitemRepository cartitemRepository;
    private final ShippingMethodRepository shippingMethodRepository;
    private final AddressRepository addressRepository;
    @Value(value = "${application.time-reserve-product}")
    private long timeReserveProduct;

    @Override
    public Page<OrderResponse> findAllByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("Audit.createdAt")));

        return orderRepository.findAllByUserId(userId,pageable).map(order -> {
            OrderResponse orderResponse = orderMapper.toOrderReponse(order);
            orderResponse.setUserId(order.getUser().getId());
            orderResponse.setShopId(order.getShop().getId());
            return orderResponse;
        });
    }

    @Override
    public Page<OrderResponse> findAllByShopId(String shopId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("Audit.createdAt")));

        return orderRepository.findAllByShopId(shopId,pageable).map(order -> {
            OrderResponse orderResponse = orderMapper.toOrderReponse(order);
            orderResponse.setUserId(order.getUser().getId());
            orderResponse.setShopId(order.getShop().getId());
            return orderResponse;
        });

    }




    @Override
    public BigDecimal handlerCheckoutInfor(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> orderRequest.getCartItemIds().contains(item.getId())).toList();



        for (CartItem item : selectedCartItems){
            Product product = productRepository.findById(item.getProduct().getId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
            totalAmount = totalAmount.add(item.getCartItemAmount());
            synchronized (product){
                LocalDateTime now = LocalDateTime.now();
                if (now.minusSeconds(timeReserveProduct).isAfter(item.getReserveAt())){
                    if (product.getStockQuantity() < item.getQuantity()){
                        throw new ApplicationException(ErrorCode.OUT_OF_STOCK);
                    }
                }

            }

        }

        return totalAmount;
    }

    @Override
    @Transactional
    public void saveOrder(OrderRequest orderRequest, BigDecimal totalAmount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> orderRequest.getCartItemIds().contains(item.getId())).toList();
        Shop shop = selectedCartItems.stream().findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)).getProduct().getShop();
        Order order = orderMapper.orderRequestToOrder(orderRequest);
        order.setStatus(StatusOrder.PENDING);
        order.setTotalAmount(totalAmount);
        order.setUser(user);
        order.setShop(shop);
        order = orderRepository.save(order);


        for (CartItem item : selectedCartItems){
            Product product = item.getProduct();
            OrderItem orderItem = OrderItem.builder()
                    .pricePerUnit(item.getPricePerProduct())
                    .quantity(item.getQuantity())
                    .totalAmount(item.getCartItemAmount())
                    .product(product)
                    .build();
            synchronized (product){
                LocalDateTime now = LocalDateTime.now();
                if (now.minusSeconds(timeReserveProduct).isAfter(item.getReserveAt())){
                    product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                    product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());
                }else{
                    product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());

                }
            }
            cartitemRepository.delete(item);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void createOrder(OrderRequest orderRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Error when check cart information"));
        List<CartItem> cartItems = cartitemRepository.findAllByCartId(cart.getId());
        List<CartItem> selectedCartItems = cartItems.stream().filter(item -> orderRequest.getCartItemIds().contains(item.getId())).toList();
        processOrderInformation(selectedCartItems);
        Shop shop = selectedCartItems.stream().findFirst().orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND)).getProduct().getShop();
        ShippingMethod shippingMethod = shippingMethodRepository.findById(orderRequest.getShippingMethodId()).orElseThrow(() -> new ApplicationException(ErrorCode.PRODUCT_NOT_FOUND));
        Address address = addressRepository.findById(orderRequest.getAddressId()).orElseThrow(() -> new ApplicationException(ErrorCode.ADDRESS_NOT_FOUND));
        Order order = new Order();
        order.setStatus(StatusOrder.PENDING);
        order.setUser(user);
        order.setShop(shop);
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setTotalAmountPaid(orderRequest.getTotalAmountPaid());
        order.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(orderRequest.getTimeDelivery()));
        order.setShippingMethod(shippingMethod);
        order.setAddress(address);
        for (CartItem item : selectedCartItems){
            Product product = item.getProduct();
            OrderItem orderItem = OrderItem.builder()
                    .pricePerUnit(item.getPricePerProduct())
                    .quantity(item.getQuantity())
                    .totalAmount(item.getCartItemAmount())
                    .product(product)
                    .build();
            synchronized (product){
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                product.setSoldQuantity(product.getSoldQuantity() + item.getQuantity());
            }
            cartitemRepository.delete(item);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
        }
        orderRepository.save(order);
    }


    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApplicationException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(StatusOrder.CANCELLED);
        orderRepository.save(order);
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
