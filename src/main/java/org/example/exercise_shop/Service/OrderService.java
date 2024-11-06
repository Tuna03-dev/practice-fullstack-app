package org.example.exercise_shop.Service;

import org.example.exercise_shop.dto.request.OrderRequest;
import org.example.exercise_shop.dto.response.OrderResponse;
import org.example.exercise_shop.dto.response.ShopOrderResponse;
import org.example.exercise_shop.entity.Order;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface OrderService {

    Page<ShopOrderResponse> findAllByUserId(String userId, int page, int size, String type, String search);
    Page<ShopOrderResponse> findAllByShopId(String shopId, int page, int size);
    BigDecimal handlerCheckoutInfor(OrderRequest orderRequest);
    void saveOrder(OrderRequest orderRequest, BigDecimal totalAmount);
    void createOrder(OrderRequest orderRequest);
    void cancelOrder(String orderId);
}
