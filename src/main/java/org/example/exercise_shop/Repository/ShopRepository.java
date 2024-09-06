package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Shop;
import org.example.exercise_shop.entity.StatusShop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ShopRepository extends JpaRepository<Shop, String> {
    List<Shop> findByStatus(StatusShop status);

    @Modifying
    @Transactional
    @Query("UPDATE Shop s set s.feePercentage = :feePercentage")
    void updateAllByFeePercentage(@Param(value = "feePercentage") double feePercentage);

    @Modifying
    @Transactional
    @Query("UPDATE Shop s set s.status = :status where s.userId = :userId and s.id != :shopId")
    void rejectOtherRequestByUserId(@Param(value = "userId")String userId, @Param(value = "shopId")String shopId, @Param(value = "status")StatusShop statusShop);


    int countByUserIdAndStatus(String userId, StatusShop statusShop);
    Shop findByUserIdAndStatus(String userId, StatusShop status);

    @Query("SELECT s FROM Shop s JOIN s.categories c " +
            "WHERE s.status = :status " +
            "AND (:categoryId IS NULL OR c.id = :categoryId) " +
            "AND (:name IS NULL OR LOWER(s.name) LIKE %:name%) " +
            "AND (:address IS NULL OR LOWER(s.address) LIKE %:address%)")
    Page<Shop> findAllShopsByFilters(
            @Param("status") StatusShop status,
            @Param("categoryId") String categoryId,
            @Param("name") String name,
            @Param("address") String address,
            Pageable pageable);
}

