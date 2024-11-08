package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Order;
import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.ShopOrderStatus;
import org.example.exercise_shop.entity.StatusOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

//    @Query("SELECT sum(o.totalAmount) from Order o where o.shop = :shop")
//    BigDecimal getTotalPrice(@Param(value = "shop")Shop shop);

//    @Query("SELECT o FROM Order o where o.shop = :shop and o.audit.createdAt between :start and :end")
//    List<Order> findByShopAndOrderDateBetween(@Param(value = "shop") Shop shop,@Param(value = "start") LocalDateTime start,@Param(value = "end") LocalDateTime end);

//    boolean existsByOrOrderItems_ProductIdAndStatus(String productId, StatusOrder status);

//    Page<Order> findAllByUserId(String userId, Pageable pageable);
//    Page<Order> findAllByShopId(String shopId, Pageable pageable);
    List<Order> findByUser_Id(String userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.shopOrders so JOIN FETCH so.orderItems oi WHERE o.user.id = :userId")
    List<Order> findOrdersWithDetailsByUserId(@Param("userId") String userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.shopOrders so JOIN FETCH o.user u JOIN FETCH so.orderItems oi where so.shop.id = :shopId and (:status is null or so.status = :status) and  (:delivery = '' or so.shippingMethod.id = :delivery)")
    List<Order> findOrderWithDetailsByShopId(@Param("shopId") String shopId, @Param("status") ShopOrderStatus status, @Param("delivery") String delivery);
}
