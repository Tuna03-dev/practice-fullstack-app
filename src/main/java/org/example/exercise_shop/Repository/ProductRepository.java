package org.example.exercise_shop.Repository;

import org.example.exercise_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product,String> {
    Page<Product> findProductsByDeleteAtIsNullAndShopId(String shopId, Pageable pageable);
    @Query("SELECT p from Product p where (:categoryId = '' or p.category.id = :categoryId) and lower(p.name) like lower(concat('%', :name,'%')) and p.deleteAt is null ")
    Page<Product> findAllByNameIsLikeIgnoreCaseAndCategory_IdAndDeleteAtIsNull(@Param(value = "name") String name, @Param(value = "categoryId") String categoryId, Pageable pageable);

    @Query("SELECT p from Product p where p.deleteAt is null  order by p.soldQuantity desc LIMIT :size")
    List<Product> findBestSellers(@Param(value = "size") int size);
    @Query("SELECT p from Product p where p.deleteAt is null and p.audit.createdAt >= :date order by p.audit.createdAt desc LIMIT :size")
    List<Product> findNewArrival(@Param(value = "size") int size, @Param(value = "date") LocalDateTime date);

    @Query("SELECT p from Product p where p.deleteAt is null order by p.audit.createdAt desc LIMIT :size")
    List<Product> findNewArrivalIfNull(@Param(value = "size") int size);

    @Query("SELECT p from Product p where p.deleteAt is null  order by p.averageRate desc LIMIT :size")
    List<Product> findTopRates(@Param(value = "size") int size);

    List<Product> findAllByIdIn(List<String> ids);
}
